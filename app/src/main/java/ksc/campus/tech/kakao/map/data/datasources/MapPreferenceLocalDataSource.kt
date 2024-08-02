package ksc.campus.tech.kakao.map.data.datasources

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.kakao.vectormap.camera.CameraPosition
import ksc.campus.tech.kakao.map.data.mapper.CameraPositionJsonMapper
import ksc.campus.tech.kakao.map.domain.models.LocationInfo
import javax.inject.Inject

interface OnMapPreferenceChanged {
    fun onCameraPositionChanged()

    fun onSelectedLocationChanged()
}

class MapPreferenceLocalDataSource @Inject constructor() {
    private val gson: Gson = Gson()
    private var sharedPreference:SharedPreferences? = null

    private fun getSharedPreference(context: Context):SharedPreferences{
        if(sharedPreference == null){
            sharedPreference = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        }
        return sharedPreference!!
    }

    class OnMapSharedPreferenceChanged(private val onPreferenceChanged: OnMapPreferenceChanged): OnSharedPreferenceChangeListener{
        override fun onSharedPreferenceChanged(
            sharedPreferences: SharedPreferences?,
            key: String?
        ) {
            Log.d("KSC", "Shared Preference Changed")
            if (key.equals(CAMERA_POSITION_KEY)) {
                onPreferenceChanged.onCameraPositionChanged()
            }
            if (key.equals(SELECTED_LOCATION_KEY)) {
                onPreferenceChanged.onSelectedLocationChanged()
            }
        }
    }

    fun getCameraPosition(context: Context): CameraPosition? {
        val data = getSharedPreference(context).getString(CAMERA_POSITION_KEY, "")

        if (data.isNullOrEmpty()) {
            return null
        }

        try {
            return CameraPositionJsonMapper.cameraPositionDeserializer.fromJson(data, CameraPosition::class.java)
        } catch (e: JsonSyntaxException) {
            Log.e("KSC", e.message ?: "")
            return null
        }
    }

    fun getSelectedLocation(context: Context): LocationInfo? {
        val data = getSharedPreference(context).getString(SELECTED_LOCATION_KEY, "")

        if (data.isNullOrEmpty()) {
            return null
        }

        try {
            return gson.fromJson(data, LocationInfo::class.java)
        } catch (e: JsonSyntaxException) {
            Log.e("KSC", e.message ?: "")
            return null
        }
    }

    fun saveCameraPosition(context: Context, position: CameraPosition) {
        val editor = getSharedPreference(context).edit()
        editor.putString(CAMERA_POSITION_KEY, CameraPositionJsonMapper.cameraPositionSerializer.toJson(position))
        editor.apply()
    }

    fun saveSelectedLocation(context: Context, location: LocationInfo) {
        val editor = getSharedPreference(context).edit()
        editor.putString(SELECTED_LOCATION_KEY, gson.toJson(location))
        editor.apply()
    }

    fun setOnPreferenceChanged(context: Context, onChanged: OnMapPreferenceChanged){
        getSharedPreference(context).registerOnSharedPreferenceChangeListener(OnMapSharedPreferenceChanged(onChanged))
        Log.d("KSC", "registering setOnPreferenceChanged")
    }

    fun clearSelectedLocation(context: Context){
        val editor = getSharedPreference(context).edit()
        editor.remove(SELECTED_LOCATION_KEY)
        editor.apply()
    }

    companion object {
        private const val PREFERENCE_NAME = "mapViewPreference"
        private const val CAMERA_POSITION_KEY = "currentPosition"
        private const val SELECTED_LOCATION_KEY = "selectedLocation"
    }
}