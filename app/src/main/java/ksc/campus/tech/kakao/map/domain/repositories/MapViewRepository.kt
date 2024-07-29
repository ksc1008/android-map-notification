package ksc.campus.tech.kakao.map.domain.repositories

import com.kakao.vectormap.camera.CameraPosition
import kotlinx.coroutines.flow.SharedFlow
import ksc.campus.tech.kakao.map.domain.models.LocationInfo

interface MapViewRepository {
    val selectedLocation: SharedFlow<LocationInfo?>
    val cameraPosition: SharedFlow<CameraPosition>

    suspend fun loadFromSharedPreference()
    suspend fun updateSelectedLocation(locationInfo: LocationInfo)
    suspend fun updateCameraPositionWithFixedZoom(latitude: Double, longitude: Double)
    suspend fun updateCameraPosition(position: CameraPosition)
    suspend fun clearSelectedLocation()
}
