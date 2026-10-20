package app.dev.manabi.database

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder

object AttendanceConverters {

    @TypeConverter
    fun attendanceMapToJson(map: Map<String, Int>?): String? {
        return map?.let { Json.encodeToString(it) }
    }

    @TypeConverter
    fun jsonToAttendanceMap(json: String?): Map<String, Int> {
        return json?.let { Json.decodeFromString(it) } ?: emptyMap()
    }
}
