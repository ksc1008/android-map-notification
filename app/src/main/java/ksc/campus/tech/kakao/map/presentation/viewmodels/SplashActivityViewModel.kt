package ksc.campus.tech.kakao.map.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ksc.campus.tech.kakao.map.R
import ksc.campus.tech.kakao.map.domain.repositories.FirebaseRemoteConfigRepository
import javax.inject.Inject

@HiltViewModel
class SplashActivityViewModel @Inject constructor(application:Application, remoteConfigRepository: FirebaseRemoteConfigRepository): AndroidViewModel(application) {
    private val _launchErrorMessage = MutableLiveData("")
    private val _shouldSwitchActivity = MutableLiveData(false)

    val appState = remoteConfigRepository.currentAppState.stateIn(
        scope = viewModelScope,
        initialValue = FirebaseRemoteConfigRepository.AppState.UNKNOWN,
        started = SharingStarted.WhileSubscribed(DEFAULT_TIMEOUT)
    )

    val shouldSwitchActivity: LiveData<Boolean>
        get() = _shouldSwitchActivity

    val launchErrorMessage:LiveData<String>
        get() = _launchErrorMessage

    init{
        viewModelScope.launch {
            remoteConfigRepository.fetchRemoteConfig()
            appState.collect{
                when(it){
                    FirebaseRemoteConfigRepository.AppState.UNAVAILABLE ->
                        _launchErrorMessage.postValue(application.resources.getString(R.string.service_unavailable_message))
                    FirebaseRemoteConfigRepository.AppState.ON_MAINTENANCE ->
                        _launchErrorMessage.postValue(application.resources.getString(R.string.maintenance_message))
                    FirebaseRemoteConfigRepository.AppState.ON_SERVICE ->
                        _shouldSwitchActivity.postValue(true)
                    else -> {
                        _launchErrorMessage.postValue("")
                    }
                }
            }
        }
    }

    companion object{
        private const val DEFAULT_TIMEOUT = 5000L
    }
}