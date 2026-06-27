package com.example.pawmesh

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pawmesh.databinding.FragmentMapBinding
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

        mapView = binding.mapView
        mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {}
            override fun onMapError(e: Exception) {}
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(kakaoMap: KakaoMap) {
                // 내 강아지 마커
                val myDogBitmap = viewToBitmap(R.layout.marker_my_dog)
                val myStyles = kakaoMap.labelManager!!.addLabelStyles(
                    LabelStyles.from(LabelStyle.from(myDogBitmap))
                )
                val myOptions = LabelOptions.from(LatLng.from(37.5665, 126.9780))
                    .setStyles(myStyles)
                kakaoMap.labelManager!!.layer!!.addLabel(myOptions)

                // 친구 강아지 마커
                val friendBitmap = viewToBitmap(R.layout.marker_friend_dog)
                val friendStyles = kakaoMap.labelManager!!.addLabelStyles(
                    LabelStyles.from(LabelStyle.from(friendBitmap))
                )
                val friendOptions = LabelOptions.from(LatLng.from(37.5700, 126.9800))
                    .setStyles(friendStyles)
                kakaoMap.labelManager!!.layer!!.addLabel(friendOptions)
            }
        })
    }

    override fun onResume() { super.onResume(); mapView.resume() }
    override fun onPause() { super.onPause(); mapView.pause() }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun viewToBitmap(layoutId: Int): Bitmap {
        val view = LayoutInflater.from(requireContext()).inflate(layoutId, null)
        view.measure(
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        val bitmap = Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

}