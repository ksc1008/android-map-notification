package ksc.campus.tech.kakao.map.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ksc.campus.tech.kakao.map.domain.repositories.FirebaseRemoteConfigRepository
import javax.inject.Inject

@HiltViewModel
class SplashActivityViewModel @Inject constructor(remoteConfigRepository: FirebaseRemoteConfigRepository): ViewModel() {
    val appState = remoteConfigRepository.currentAppState.stateIn(
        scope = viewModelScope,
        initialValue = FirebaseRemoteConfigRepository.AppState.UNKNOWN,
        started = SharingStarted.WhileSubscribed(DEFAULT_TIMEOUT)
    )

    init{
        viewModelScope.launch {
            remoteConfigRepository.fetchRemoteConfig()
        }
    }

    companion object{
        private const val DEFAULT_TIMEOUT = 5000L
    }
}