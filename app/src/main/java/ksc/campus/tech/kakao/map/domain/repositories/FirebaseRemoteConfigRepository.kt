package ksc.campus.tech.kakao.map.domain.repositories

import kotlinx.coroutines.flow.SharedFlow

interface FirebaseRemoteConfigRepository {
    enum class AppState {ON_SERVICE, ON_MAINTENANCE, UNAVAILABLE, UNKNOWN}

    val currentAppState:SharedFlow<AppState>
    val appConfigMessage:SharedFlow<String>

    suspend fun fetchRemoteConfig()
}