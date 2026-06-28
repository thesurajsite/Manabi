package app.dev.manabi.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Attendance(
    val id: Long,
    val subjectName: String,
    val conducted: Int,
    val attended: Int,
    val requirement: Int,
    val days: List<String>,
    val teacher: String,
    val createdAt: String,
    val updatedAt: String,
)
