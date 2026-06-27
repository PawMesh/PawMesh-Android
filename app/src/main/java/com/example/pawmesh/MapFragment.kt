package com.example.pawmesh

import android.annotation.SuppressLint
import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import androidx.core.graphics.toColorInt
import androidx.fragment.app.Fragment
import com.example.pawmesh.databinding.FragmentMapBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles

class MapFragment : Fragment() {
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private lateinit var mapView: MapView
    private var kakaoMap: KakaoMap? = null

    // GPS
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var myLat = 0.0
    private var myLng = 0.0

    // 상태
    private var isFindingFriend = false
    private var walkId: Int? = null

    // 폴링
    private val handler = Handler(Looper.getMainLooper())
    private val locationRunnable = object : Runnable {
        override fun run() {
            updateMyLocation()
            fetchNearbyDogs()
            handler.postDelayed(this, 10_000L) // 10초마다
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

        // 둘 중 하나라도 허용되었다면 진행
        if (fineGranted || coarseGranted) {
            if (isFindingFriend) {
                updateMyLocationAndStartWalk()
            }
        } else {
            Toast.makeText(requireContext(), "위치 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
            isFindingFriend = false
            updateToggleUI()
        }
    }

    companion object {
        fun newInstance() = MapFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        setupToggle()
        setupMap()
    }

    // ── 토글 UI ──────────────────────────────────────────

    private fun setupToggle() {
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

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
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

    @SuppressLint("MissingPermission")
    private fun updateMyLocationAndStartWalk() {
        getMyLocation { lat, lng ->
            myLat = lat; myLng = lng
            startWalk(lat, lng)
        }
    }

    private fun activateAloneWalk() {
        isFindingFriend = false
        updateToggleUI()

        // 폴링 중단 + 산책 종료 API + 친구 마커 제거
        handler.removeCallbacks(locationRunnable)
        endWalk()
        clearFriendMarkers()
    }

    // ── 지도 ──────────────────────────────────────────

    private fun setupMap() {
        mapView = binding.mapView
        mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() { mapView.pause() }
            override fun onMapError(e: Exception) { e.printStackTrace() }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(map: KakaoMap) {
                kakaoMap = map
                addMyMarker()
            }
        })
    }

    private fun addMyMarker() {
        val map = kakaoMap ?: return
        val layer = map.labelManager?.layer ?: return
        val bitmap = viewToBitmap(R.layout.marker_my_dog)
        val styles = map.labelManager!!.addLabelStyles(LabelStyles.from(LabelStyle.from(bitmap)))
        val lat = if (myLat != 0.0) myLat else 37.5665
        val lng = if (myLng != 0.0) myLng else 126.9780
        layer.addLabel(LabelOptions.from("my_dog", LatLng.from(lat, lng)).setStyles(styles))
    }

    // 친구 마커 관리
    private val friendLabelIds = mutableListOf<String>()

    private fun addFriendMarkers(dogs: List<NearbyDog>) {
        val map = kakaoMap ?: return
        val layer = map.labelManager?.layer ?: return
        clearFriendMarkers()

        dogs.forEach { dog ->
            val bitmap = viewToBitmap(R.layout.marker_friend_dog)
            val styles = map.labelManager!!.addLabelStyles(LabelStyles.from(LabelStyle.from(bitmap)))
            val labelId = "friend_${dog.dogId}"
            layer.addLabel(
                LabelOptions.from(labelId, LatLng.from(dog.latitude, dog.longitude)).setStyles(styles)
            )
            friendLabelIds.add(labelId)
        }
    }

    private fun clearFriendMarkers() {
        val layer = kakaoMap?.labelManager?.layer ?: return
        friendLabelIds.forEach { id ->
            val label = layer.getLabel(id)
            label?.let { layer.remove(it) }
            }
            friendLabelIds.clear()
    }

    // ── GPS ──────────────────────────────────────────

    @RequiresPermission(anyOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    private fun getMyLocation(onResult: (Double, Double) -> Unit) {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let { onResult(it.latitude, it.longitude) }
        }
    }

    @SuppressLint("MissingPermission")
    private fun updateMyLocation() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            getMyLocation { lat, lng ->
                myLat = lat; myLng = lng
                walkId?.let { id -> patchLocation(id, lat, lng) }
            }
        }
    }

    // ── API 호출 ──────────────────────────────────────────

    private fun startWalk(lat: Double, lng: Double) {
        // TODO: Retrofit - POST /walk/start
        // body: { dogId, latitude, longitude }
        // response: { walkId }
        // walkId = response.walkId
        handler.post(locationRunnable) // 폴링 시작
    }

    private fun patchLocation(walkId: Int, lat: Double, lng: Double) {
        // TODO: Retrofit - PATCH /walk/location
        // body: { walkId, latitude, longitude }
    }

    private fun fetchNearbyDogs() {
        if (!isFindingFriend) return
        // TODO: Retrofit - GET /dogs/nearby?latitude=&longitude=&radius=1000
        // response: List<NearbyDog>
        // addFriendMarkers(response)
    }

    private fun endWalk() {
        walkId?.let {
            // TODO: Retrofit - POST /walk/end
            // body: { walkId }
            walkId = null
        }
    }

    // ── 생명주기 ──────────────────────────────────────────

    override fun onResume() {
        super.onResume()
        if (::mapView.isInitialized) mapView.resume()
    }

    override fun onPause() {
        super.onPause()
        if (::mapView.isInitialized) mapView.pause()
        handler.removeCallbacks(locationRunnable)
    }

    override fun onDestroyView() {
        super.onDestroyView()
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

    data class NearbyDog(
        val dogId: Int,
        val name: String,
        val latitude: Double,
        val longitude: Double
    )
}