package app.dev.manabi.domain.model

data class Subject(
    val id: Long,
    val subjectName: String,
    val conducted: Int,
    val attended: Int,
    val requirement: Int,
    val createdAt: String,
    val updatedAt: String,
    val days: List<String>
)