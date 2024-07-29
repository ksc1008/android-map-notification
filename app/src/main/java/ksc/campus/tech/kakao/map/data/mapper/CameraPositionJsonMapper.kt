package ksc.campus.tech.kakao.map.data.mapper

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.kakao.vectormap.camera.CameraPosition
import java.lang.reflect.Type

class CameraPositionSerializer : JsonSerializer<CameraPosition> {
    override fun serialize(
        src: CameraPosition?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        val json = JsonObject()

        if (src == null) {
            return json
        }

        json.addProperty("latitude", src.position.latitude)
        json.addProperty("longitude", src.position.longitude)
        json.addProperty("zoom_level", src.zoomLevel)
        json.addProperty("tilt_angle", src.tiltAngle)
        json.addProperty("rotation_angle", src.rotationAngle)
        json.addProperty("height", src.height)

        return json
    }
}

class CameraPositionDeserializer : JsonDeserializer<CameraPosition?> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): CameraPosition? {
        val nullCheckString = json?.asJsonPrimitive?.asString
        if (nullCheckString.isNullOrEmpty()) {
            return null
        }

        try {
            val jsonObject = json.asJsonObject
            return CameraPosition.from(
                jsonObject.get("latitude").asDouble,
                jsonObject.get("longitude").asDouble,
                jsonObject.get("zoom_level").asInt,
                jsonObject.get("tilt_angle").asDouble,
                jsonObject.get("rotation_angle").asDouble,
                jsonObject.get("height").asDouble
            )
        } catch (e: UnsupportedOperationException) {
            Log.e("KSC", e.message ?: "")
            return null
        } catch (e: NumberFormatException) {
            Log.e("KSC", e.message ?: "")
            return null
        } catch (e: IllegalStateException) {
            Log.e("KSC", e.message ?: "")
            return null
        }
    }
}

object CameraPositionJsonMapper {
    val cameraPositionSerializer: Gson = GsonBuilder()
        .registerTypeAdapter(CameraPositionSerializer::class.java, CameraPositionSerializer())
        .create()
    val cameraPositionDeserializer: Gson = GsonBuilder()
        .registerTypeAdapter(CameraPositionDeserializer::class.java, CameraPositionDeserializer())
        .create()
}