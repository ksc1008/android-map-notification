package ksc.campus.tech.kakao.map.data.repositoryimpls

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.flow
import ksc.campus.tech.kakao.map.data.datasources.FirebaseMapConfigRemoteDataSource
import ksc.campus.tech.kakao.map.data.mapper.FirebaseRemoteConfigMapper
import ksc.campus.tech.kakao.map.domain.models.AppServiceState
import ksc.campus.tech.kakao.map.domain.repositories.FirebaseRemoteConfigRepository
import ksc.campus.tech.kakao.map.domain.repositories.FirebaseRemoteConfigRepository.AppState.ON_MAINTENANCE
import ksc.campus.tech.kakao.map.domain.repositories.FirebaseRemoteConfigRepository.AppState.ON_SERVICE
import ksc.campus.tech.kakao.map.domain.repositories.FirebaseRemoteConfigRepository.AppState.UNAVAILABLE
import javax.inject.Inject

class FirebaseRemoteConfigRepositoryImpl @Inject constructor(
    private val firebaseMapConfigRemoteDataSource: FirebaseMapConfigRemoteDataSource
) : FirebaseRemoteConfigRepository {
    override val currentAppState: Flow<AppServiceState> =
        flow {
            while (true) {
                emit(fetchRemoteConfig())
                delay(5000)
            }
        }

    private suspend fun fetchRemoteConfig(): AppServiceState {
        val config = firebaseMapConfigRemoteDataSource.fetchAppState()

        return FirebaseRemoteConfigMapper.mapRemoteConfigDataToAppServiceState(config)
    }
}