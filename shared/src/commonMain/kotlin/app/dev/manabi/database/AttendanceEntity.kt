package app.dev.manabi.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Subject")
data class AttendanceEntity(
    @PrimaryKey val id: Long,
    val subjectName: String,
    val conducted: Long,
    val attended: Long,
    val requirement: Long,
    val days: Map<String, Int>,
    val teacher: String,
    val createdAt: String,
    val updatedAt: String
)
