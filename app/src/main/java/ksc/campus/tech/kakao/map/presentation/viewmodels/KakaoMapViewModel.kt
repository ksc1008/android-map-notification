package ksc.campus.tech.kakao.map.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.vectormap.camera.CameraPosition
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ksc.campus.tech.kakao.map.domain.repositories.MapViewRepository
import javax.inject.Inject

@HiltViewModel
class KakaoMapViewModel @Inject constructor(
    private val mapViewRepository: MapViewRepository,
    ): ViewModel(){

    private val _mapViewState = MutableLiveData(MapViewState.INACTIVE)
    private val _kakaoMapErrorMessage = MutableLiveData("")

    val mapViewState: LiveData<MapViewState> = _mapViewState
    val kakaoMapErrorMessage: LiveData<String> = _kakaoMapErrorMessage
    val cameraPosition = mapViewRepository.cameraPosition

    private fun getErrorMessage(error: String): String {
        return "지도 인증을 실패 했습니다. \n다시 시도해 주세요.\n\n$error"
    }

    fun updateCameraPosition(position: CameraPosition) {
        viewModelScope.launch(Dispatchers.IO) {
            mapViewRepository.updateCameraPosition(position)
        }
    }

    fun pause(){
        _mapViewState.postValue(MapViewState.INACTIVE)
    }

    fun resumeMap(){
        _mapViewState.postValue(MapViewState.ACTIVE)
    }

    fun onMapError(errorMessage: String){
        val text = getErrorMessage(errorMessage)
        _kakaoMapErrorMessage.postValue(text)
        _mapViewState.postValue(MapViewState.ERROR)
    }

    fun startMap(){
        _mapViewState.postValue(MapViewState.STARTING)
    }

    enum class MapViewState {INACTIVE, STARTING, ERROR, ACTIVE}
}