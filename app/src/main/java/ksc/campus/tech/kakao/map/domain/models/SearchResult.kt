package ksc.campus.tech.kakao.map.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class SearchResult(
    val id: String,
    val name: String,
    val address: String,
    val type: String,
    val latitude: Double,
    val longitude: Double
)