package ksc.campus.tech.kakao.map.data.repositoryimpls

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import ksc.campus.tech.kakao.map.data.datasources.FirebaseMapConfigRemoteDataSource
import ksc.campus.tech.kakao.map.domain.repositories.FirebaseRemoteConfigRepository
import ksc.campus.tech.kakao.map.domain.repositories.FirebaseRemoteConfigRepository.AppState.ON_MAINTENANCE
import ksc.campus.tech.kakao.map.domain.repositories.FirebaseRemoteConfigRepository.AppState.ON_SERVICE
import ksc.campus.tech.kakao.map.domain.repositories.FirebaseRemoteConfigRepository.AppState.UNAVAILABLE
import javax.inject.Inject

class FirebaseRemoteConfigRepositoryImpl @Inject constructor(
    private val firebaseMapConfigRemoteDataSource: FirebaseMapConfigRemoteDataSource
): FirebaseRemoteConfigRepository {
    private val _currentAppState = MutableSharedFlow<FirebaseRemoteConfigRepository.AppState>()
    private val _appConfigMessage = MutableSharedFlow<String>()

    override val currentAppState: SharedFlow<FirebaseRemoteConfigRepository.AppState>
        get() = _currentAppState

    override val appConfigMessage: SharedFlow<String>
        get() = _appConfigMessage

    override suspend fun fetchRemoteConfig() {
        val config = firebaseMapConfigRemoteDataSource.fetchAppState()

        when(config?.serviceState){
            FIREBASE_APP_STATE_ON_SERVICE -> _currentAppState.emit(ON_SERVICE)
            FIREBASE_APP_STATE_ON_MAINTENANCE -> _currentAppState.emit(ON_MAINTENANCE)
            FIREBASE_APP_STATE_UNAVAILABLE -> _currentAppState.emit(UNAVAILABLE)
            else -> _currentAppState.emit(UNAVAILABLE)
        }

        config?.serviceMessage?.let {
            _appConfigMessage.emit(it)
        }
    }

    companion object {
        const val FIREBASE_APP_STATE_ON_SERVICE = "ON_SERVICE"
        const val FIREBASE_APP_STATE_ON_MAINTENANCE = "ON_MAINTENANCE"
        const val FIREBASE_APP_STATE_UNAVAILABLE = "UNAVAILABLE"
    }
}