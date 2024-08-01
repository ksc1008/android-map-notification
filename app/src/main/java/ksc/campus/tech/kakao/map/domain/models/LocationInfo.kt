package ksc.campus.tech.kakao.map.domain.models

data class LocationInfo(
    val address: String,
    val name: String,
    val latitude: Double,
    val longitude: Double
)