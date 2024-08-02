package ksc.campus.tech.kakao.map.domain.models

import ksc.campus.tech.kakao.map.domain.repositories.FirebaseRemoteConfigRepository

data class AppServiceState(val serviceState: FirebaseRemoteConfigRepository.AppState,
                           val serviceMessage:String)