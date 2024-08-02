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
import ksc.campus.tech.kakao.map.domain.models.AppServiceState
import ksc.campus.tech.kakao.map.domain.repositories.FirebaseRemoteConfigRepository
import javax.inject.Inject

@HiltViewModel
class SplashActivityViewModel @Inject constructor(application:Application, remoteConfigRepository: FirebaseRemoteConfigRepository): AndroidViewModel(application) {

    val appState = remoteConfigRepository.currentAppState.stateIn(
        scope = viewModelScope,
        initialValue = AppServiceState(FirebaseRemoteConfigRepository.AppState.UNKNOWN,""),
        started = SharingStarted.WhileSubscribed(DEFAULT_TIMEOUT)
    )

    companion object{
        private const val DEFAULT_TIMEOUT = 5000L
    }
}