package ksc.campus.tech.kakao.map.domain.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import ksc.campus.tech.kakao.map.domain.models.AppServiceState

interface FirebaseRemoteConfigRepository {
    enum class AppState {ON_SERVICE, ON_MAINTENANCE, UNAVAILABLE, UNKNOWN}

    val currentAppState: Flow<AppServiceState>
}