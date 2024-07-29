package ksc.campus.tech.kakao.map.data.repositoryimpls

import android.content.Context
import android.util.Log
import com.kakao.vectormap.camera.CameraPosition
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import ksc.campus.tech.kakao.map.data.datasources.MapPreferenceLocalDataSource
import ksc.campus.tech.kakao.map.data.datasources.OnMapPreferenceChanged
import ksc.campus.tech.kakao.map.domain.models.LocationInfo
import ksc.campus.tech.kakao.map.domain.repositories.MapViewRepository
import javax.inject.Inject


class MapViewRepositoryImpl @Inject constructor(
    private val mapPreferenceDataSource: MapPreferenceLocalDataSource,
    @ApplicationContext private val context:Context
) : MapViewRepository {

    private var _selectedLocation = MutableSharedFlow<LocationInfo?>(
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    private var _cameraPosition = MutableSharedFlow<CameraPosition>(
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    override val selectedLocation: SharedFlow<LocationInfo?>
        get() = _selectedLocation

    override val cameraPosition: SharedFlow<CameraPosition>
        get() = _cameraPosition

    private fun getZoomCameraPosition(latitude: Double, longitude: Double) = CameraPosition.from(
        latitude,
        longitude,
        ZOOMED_CAMERA_ZOOM_LEVEL,
        ZOOMED_CAMERA_TILT_ANGLE, ZOOMED_CAMERA_ROTATION_ANGLE,
        ZOOMED_CAMERA_HEIGHT
    )

    init {
        mapPreferenceDataSource.setOnPreferenceChanged(context, object: OnMapPreferenceChanged {
            override fun onCameraPositionChanged() {
                CoroutineScope(Dispatchers.IO).launch {
                    val updated = loadSavedCurrentPosition()
                    _cameraPosition.emit(updated)
                    Log.d("KSC","Updated position")
                }
            }

            override fun onSelectedLocationChanged() {
                CoroutineScope(Dispatchers.IO).launch {
                    val updated = loadSavedSelectedLocation()
                    _selectedLocation.emit(updated)
                    Log.d("KSC","Updated location")
                }
            }
        })
        CoroutineScope(Dispatchers.IO).launch {
            _cameraPosition.emit(loadSavedCurrentPosition())
            _selectedLocation.emit(loadSavedSelectedLocation())
        }
    }

    private fun saveCurrentPositionToSharedPreference(position: CameraPosition){
        mapPreferenceDataSource.saveCameraPosition(context, position)
    }

    private fun saveSelectedLocation(location: LocationInfo){
        mapPreferenceDataSource.saveSelectedLocation(context, location)
    }

    private fun loadSavedCurrentPosition(): CameraPosition {
        val data = mapPreferenceDataSource.getCameraPosition(context)
        return data?: initialCameraPosition
    }

    private fun loadSavedSelectedLocation(): LocationInfo? {
        return mapPreferenceDataSource.getSelectedLocation(context)
    }

    override suspend fun loadFromSharedPreference(){
        val cameraPosition = loadSavedCurrentPosition()
        val selectedLocation = loadSavedSelectedLocation()

        updateCameraPosition(cameraPosition)
        if(selectedLocation != null)
            updateSelectedLocation(selectedLocation)
    }

    override suspend fun updateSelectedLocation(locationInfo: LocationInfo){
        saveSelectedLocation(locationInfo)
    }

    override suspend fun updateCameraPositionWithFixedZoom(latitude: Double, longitude: Double){
        updateCameraPosition(getZoomCameraPosition(latitude, longitude))
        Log.d("KSC", "(fixed zoom) updated position to ${latitude},${longitude}")
    }

    override suspend fun updateCameraPosition(position: CameraPosition){
        saveCurrentPositionToSharedPreference(position)
    }

    override suspend fun clearSelectedLocation(){
        mapPreferenceDataSource.clearSelectedLocation(context)
    }

    companion object {
        private const val ZOOMED_CAMERA_ZOOM_LEVEL = 18
        private const val ZOOMED_CAMERA_TILT_ANGLE = 0.0
        private const val ZOOMED_CAMERA_ROTATION_ANGLE = 0.0
        private const val ZOOMED_CAMERA_HEIGHT = -1.0

        val initialCameraPosition: CameraPosition = CameraPosition.from(
            35.8905341232321,
            128.61213266480294,
            15,
            0.0,
            0.0,
            -1.0
        )
    }
}