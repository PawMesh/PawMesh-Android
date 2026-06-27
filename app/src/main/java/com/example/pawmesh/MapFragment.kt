package com.example.pawmesh

import android.annotation.SuppressLint
import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import androidx.core.graphics.toColorInt
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.pawmesh.data.network.dto.request.CreateMapSessionRequestDto
import com.example.pawmesh.data.network.dto.request.LocationUpdateRequestDto
import com.example.pawmesh.data.network.dto.response.NearbySessionDto
import com.example.pawmesh.data.network.retrofit.RetrofitClient
import com.example.pawmesh.databinding.FragmentMapBinding
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import kotlinx.coroutines.launch

class MapFragment : Fragment() {
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private lateinit var mapView: MapView
    private var kakaoMap: KakaoMap? = null

    // GPS
    private var myLat = 0.0
    private var myLng = 0.0

    // 상태
    private var isFindingFriend = false
    private var walkId: Long? = null

    private var mapStarted = false

    // 폴링
    private val handler = Handler(Looper.getMainLooper())
    private val locationRunnable = object : Runnable {
        override fun run() {
            updateMyLocation()
            fetchNearbyDogs()
            handler.postDelayed(this, 10_000L)
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) {
            if (isFindingFriend) updateMyLocationAndStartWalk()
        } else {
            Toast.makeText(requireContext(), "위치 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
            isFindingFriend = false
            updateToggleUI()
        }
    }

    companion object {
        fun newInstance() = MapFragment()
        private const val TAG = "MapFragment"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "onCreateView")
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        Log.d(TAG, "binding inflated: root=${binding.root}")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: view=$view")
        setupToggle()
        setupMap()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    // ── 토글 UI ──────────────────────────────────────────

    private fun setupToggle() {
        Log.d(TAG, "setupToggle")
        binding.btnFindFriend.setOnClickListener {
            if (!isFindingFriend) activateFindFriend()
        }
        binding.btnAloneWalk.setOnClickListener {
            if (isFindingFriend) activateAloneWalk()
        }
    }

    private fun activateFindFriend() {
        isFindingFriend = true
        updateToggleUI()

        val hasFine = ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val hasCoarse = ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (hasFine || hasCoarse) {
            updateMyLocationAndStartWalk()
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun activateAloneWalk() {
        isFindingFriend = false
        updateToggleUI()
        handler.removeCallbacks(locationRunnable)
        endWalk()
        clearFriendMarkers()
    }

    private fun updateToggleUI() {
        if (isFindingFriend) {
            binding.btnFindFriend.setBackgroundResource(R.drawable.bg_toggle_selected)
            binding.btnFindFriend.setTextColor(Color.WHITE)
            binding.btnAloneWalk.setBackgroundColor(Color.TRANSPARENT)
            binding.btnAloneWalk.setTextColor("#999999".toColorInt())
        } else {
            binding.btnAloneWalk.setBackgroundResource(R.drawable.bg_toggle_selected)
            binding.btnAloneWalk.setTextColor(Color.WHITE)
            binding.btnFindFriend.setBackgroundColor(Color.TRANSPARENT)
            binding.btnFindFriend.setTextColor("#999999".toColorInt())
        }
    }

    // ── 지도 ──────────────────────────────────────────

    private fun setupMap() {
        Log.d(TAG, "setupMap")
        mapView = binding.mapView
        try {
            mapView.start(object : MapLifeCycleCallback() {
                override fun onMapDestroy() {
                    Log.d(TAG, "onMapDestroy")
                }
                override fun onMapError(e: Exception) {
                    Log.e(TAG, "onMapError: ${e.message}", e)
                }
            }, object : KakaoMapReadyCallback() {
                override fun onMapReady(map: KakaoMap) {
                    Log.d(TAG, "onMapReady")
                    kakaoMap = map
                    mapStarted = true
                    addMyMarker()
                    map.setOnLabelClickListener { _, _, label ->
                        if (sessionMap.containsKey(label.labelId) || label.labelId == "test_pin") {
                            startActivity(Intent(requireContext(), WalkCompleteActivity::class.java))
                        }
                        true
                    }
                }
            })
        } catch (e: Exception) {
            Log.e(TAG, "mapView.start() failed", e)
        }
    }

    private fun addMyMarker() {
        Log.d(TAG, "addMyMarker: kakaoMap=$kakaoMap")
        val map = kakaoMap ?: run { Log.e(TAG, "addMyMarker: kakaoMap is null"); return }
        val layer = map.labelManager?.layer ?: run { Log.e(TAG, "addMyMarker: labelManager or layer is null"); return }
        Log.d(TAG, "addMyMarker: layer=$layer")
        val bitmap = viewToBitmap(R.layout.marker_my_dog)
        Log.d(TAG, "addMyMarker: bitmap=${bitmap.width}x${bitmap.height}")
        val styles = map.labelManager!!.addLabelStyles(LabelStyles.from(LabelStyle.from(bitmap)))
        val lat = if (myLat != 0.0) myLat else 37.5665
        val lng = if (myLng != 0.0) myLng else 126.9780
        Log.d(TAG, "addMyMarker: lat=$lat, lng=$lng")
        layer.addLabel(LabelOptions.from("my_dog", LatLng.from(lat, lng)).setStyles(styles))
        Log.d(TAG, "addMyMarker: label added")
    }

    // 친구 마커 관리
    private val friendLabelIds = mutableListOf<String>()
    private val sessionMap = mutableMapOf<String, NearbySessionDto>()
    private fun addTestPin() {
        val map = kakaoMap ?: return
        val layer = map.labelManager?.layer ?: return

        val latLng = LatLng.from(37.1112, 126.9780) // 서울시청 근처 친구 개 (무조건 보이는 위치)

        val bitmap = viewToBitmap(R.layout.marker_friend_dog)

        Log.d(TAG, "pin bitmap = ${bitmap.width}x${bitmap.height}")

        val styles = map.labelManager!!.addLabelStyles(
            LabelStyles.from(LabelStyle.from(bitmap))
        )

        layer.addLabel(
            LabelOptions.from("test_pin", latLng)
                .setStyles(styles)
        )

        Log.d(TAG, "pin added")
    }

    private fun clearFriendMarkers() {
        val layer = kakaoMap?.labelManager?.layer ?: return
        friendLabelIds.forEach { id ->
            val label = layer.getLabel(id)
            label?.let { layer.remove(it) }
        }
        friendLabelIds.clear()
        sessionMap.clear()
    }

    // ── GPS ──────────────────────────────────────────

    @SuppressLint("MissingPermission")
    private fun getLocation(): Pair<Double, Double>? {
        val lm = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return try {
            val loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                ?: lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            loc?.let { it.latitude to it.longitude }
        } catch (_: SecurityException) {
            null
        }
    }

    private fun updateMyLocationAndStartWalk() {
        getLocation()?.let { (lat, lng) ->
            myLat = lat
            myLng = lng
            startWalk(lat, lng)
        }
    }

    private fun updateMyLocation() {
        getLocation()?.let { (lat, lng) ->
            myLat = lat
            myLng = lng
            walkId?.let { id -> patchLocation(id, lat, lng) }
        }
    }

    // ── API 호출 ──────────────────────────────────────────

    private fun token(): String {
        val prefs = requireContext().getSharedPreferences("pawmesh_prefs", Context.MODE_PRIVATE)
        return "Bearer ${prefs.getString("access_token", "")}"
    }

    private fun startWalk(lat: Double, lng: Double) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val prefs = requireContext().getSharedPreferences("pawmesh_prefs", Context.MODE_PRIVATE)
                val dogId = prefs.getInt("dog_id", 1)
                val resp = RetrofitClient.mapApi.createMapSession(
                    token(),
                    CreateMapSessionRequestDto(dogId, lat, lng)
                )
                if (resp.isSuccessful) {
                    walkId = resp.body()?.data?.walkSessionId?.toLong()
                    Log.d(TAG, "세션 생성 완료: walkId=$walkId")
                    handler.post(locationRunnable)
                } else {
                    Log.w(TAG, "세션 생성 실패: ${resp.code()}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "세션 생성 오류", e)
            }
        }
    }

    private fun patchLocation(sessionId: Long, lat: Double, lng: Double) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val resp = RetrofitClient.mapApi.updateLocation(
                    token(),
                    sessionId,
                    LocationUpdateRequestDto(lat, lng)
                )
                if (!resp.isSuccessful) Log.w(TAG, "위치 업데이트 실패: ${resp.code()}")
            } catch (e: Exception) {
                Log.e(TAG, "위치 업데이트 오류", e)
            }
        }
    }

    private fun fetchNearbyDogs() {
        if (!isFindingFriend) return
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val resp = RetrofitClient.mapApi.getNearbySessions(token(), myLat, myLng)
                if (resp.isSuccessful) {
                    addTestPin()
                } else {
                    Log.w(TAG, "주변 세션 조회 실패: ${resp.code()}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "주변 세션 조회 오류", e)
            }
        }
    }

    private fun endWalk() {
        val id = walkId ?: return
        walkId = null
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val resp = RetrofitClient.mapApi.deleteMapSession(token(), id)
                if (!resp.isSuccessful) Log.w(TAG, "세션 종료 실패: ${resp.code()}")
            } catch (e: Exception) {
                Log.e(TAG, "세션 종료 오류", e)
            }
        }
    }

    // ── 생명주기 ──────────────────────────────────────────

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: mapStarted=$mapStarted")
        if (mapStarted) mapView.resume()
    }

    override fun onPause() {
        Log.d(TAG, "onPause")
        if (mapStarted) mapView.pause()
        handler.removeCallbacks(locationRunnable)
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView")
        _binding = null
    }

    // ── 유틸 ──────────────────────────────────────────

    private fun viewToBitmap(layoutId: Int): Bitmap {
        val view = LayoutInflater.from(requireContext()).inflate(layoutId, null)
        view.measure(
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        val bitmap = createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
        Canvas(bitmap).also { view.draw(it) }
        return bitmap
    }
}
