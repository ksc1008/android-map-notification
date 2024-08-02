package ksc.campus.tech.kakao.map.data.mapper

import ksc.campus.tech.kakao.map.data.entities.RemoteConfigData
import ksc.campus.tech.kakao.map.data.repositoryimpls.FirebaseRemoteConfigRepositoryImpl
import ksc.campus.tech.kakao.map.domain.models.AppServiceState
import ksc.campus.tech.kakao.map.domain.repositories.FirebaseRemoteConfigRepository

object FirebaseRemoteConfigMapper {
    fun mapRemoteConfigDataToAppServiceState(remoteConfigData: RemoteConfigData?): AppServiceState {
        val state:FirebaseRemoteConfigRepository.AppState
        when(remoteConfigData?.serviceState){
            FIREBASE_APP_STATE_ON_SERVICE ->
                state = FirebaseRemoteConfigRepository.AppState.ON_SERVICE
            FIREBASE_APP_STATE_ON_MAINTENANCE ->
                state = FirebaseRemoteConfigRepository.AppState.ON_MAINTENANCE
            FIREBASE_APP_STATE_UNAVAILABLE ->
                state = FirebaseRemoteConfigRepository.AppState.UNAVAILABLE
            else ->
                state = FirebaseRemoteConfigRepository.AppState.UNAVAILABLE
        }
        val message =
            remoteConfigData?.serviceMessage?:""
        return AppServiceState(state, message)
    }

    private const val FIREBASE_APP_STATE_ON_SERVICE = "ON_SERVICE"
    private const val FIREBASE_APP_STATE_ON_MAINTENANCE = "ON_MAINTENANCE"
    private const val FIREBASE_APP_STATE_UNAVAILABLE = "UNAVAILABLE"
}