package ksc.campus.tech.kakao.map.presentation.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.camera.CameraPosition
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ksc.campus.tech.kakao.map.R
import ksc.campus.tech.kakao.map.databinding.FragmentKakaoMapBinding
import ksc.campus.tech.kakao.map.domain.models.LocationInfo
import ksc.campus.tech.kakao.map.presentation.viewmodels.KakaoMapViewModel
import ksc.campus.tech.kakao.map.presentation.viewmodels.SearchActivityViewModel

@AndroidEntryPoint
class KakaoMapFragment:
    Fragment() {

    private val viewModel: SearchActivityViewModel by activityViewModels()
    private val mapViewModel: KakaoMapViewModel by lazy{ViewModelProvider(requireActivity())[KakaoMapViewModel::class.java]}

    private lateinit var binding: FragmentKakaoMapBinding

    private var kakaoMap: KakaoMap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentKakaoMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initiateViewModelCallbacks()
        startKakaoMapView()
        initiateBinding()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initiateBinding(){
        binding.viewModel = mapViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.rootFragment = this
    }

    private fun startKakaoMapView() {
        mapViewModel.startMap()
        binding.kakaoMapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
            }

            override fun onMapError(e: Exception?) {
                mapViewModel.onMapError(e?.message?:"")
            }

        },
            object : KakaoMapReadyCallback() {
                override fun onMapReady(km: KakaoMap) {
                    kakaoMap = km
                    mapViewModel.resumeMap()
                    restorePosition()
                    restoreMarker()
                }
            })
    }

    private fun setKakaoMapMoveListener(){
        kakaoMap?.setOnCameraMoveEndListener { _, position, _ ->
            mapViewModel.updateCameraPosition(position)
        }
    }

    private fun skipNextCameraMoveListener(){
        kakaoMap?.setOnCameraMoveEndListener { _, _, _ ->
            setKakaoMapMoveListener()
        }
    }

    private fun moveCamera(position: CameraPosition) {
        if (kakaoMap?.isVisible != true) {
            return
        }
        skipNextCameraMoveListener()

        val camUpdate =
            CameraUpdateFactory.newCameraPosition(position)
        kakaoMap?.moveCamera(camUpdate)
    }

    private fun initiateViewModelCallbacks() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED){
                mapViewModel.cameraPosition.collect{
                    moveCamera(it)
                }
            }

            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel.selectedLocation.collect{
                    updateSelectedLocation(it)
                }
            }
        }
    }

    fun restartKakaoMap(){
        startKakaoMapView()
    }

    override fun onResume() {
        mapViewModel.resumeMap()
        binding.kakaoMapView.resume()
        super.onResume()
    }

    override fun onPause() {
        mapViewModel.pause()
        binding.kakaoMapView.pause()
        super.onPause()
    }

    override fun onDestroyView() {
        binding.kakaoMapView.finish()
        mapViewModel.pause()
        super.onDestroyView()
    }

    private fun restoreMarker() {
        updateSelectedLocation(viewModel.selectedLocation.replayCache.last())
    }

    private fun restorePosition() {
        moveCamera(mapViewModel.cameraPosition.replayCache.last())
    }

    private fun updateSelectedLocation(locationInfo: LocationInfo?) {
        if (locationInfo == null) {
            clearLabels()
        } else {
            changeSelectedPosition(LatLng.from(locationInfo.latitude, locationInfo.longitude))
        }
    }

    private fun changeSelectedPosition(coordinate: LatLng) {
        clearLabels()
        addLabel(coordinate)
    }

    private fun clearLabels() {
        if (kakaoMap?.isVisible != true) {
            return
        }
        val layer = kakaoMap?.labelManager?.layer
        layer?.removeAll()
    }

    private fun addLabel(coordinate: LatLng) {
        if (kakaoMap?.isVisible != true) {
            return
        }
        val styles = kakaoMap?.labelManager
            ?.addLabelStyles(LabelStyles.from(LabelStyle.from(R.drawable.map_pin)))
        val options = LabelOptions.from(coordinate)
        options.setStyles(styles)
        val layer = kakaoMap?.labelManager?.layer
        layer?.addLabel(options)
    }
}