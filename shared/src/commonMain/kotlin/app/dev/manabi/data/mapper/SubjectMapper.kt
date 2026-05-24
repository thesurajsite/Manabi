package app.dev.manabi.data.mapper

import app.dev.manabi.data.model.Subject
import app.dev.manabi.database.Subject as SubjectEntity

fun SubjectEntity.toDomain(): Subject {
    return Subject(
        id = id,
        subjectName = subjectName,
        conducted = conducted.toInt(),
        attended = attended.toInt(),
        requirement = requirement.toInt(),
        createdAt = createdAt,
        updatedAt = updatedAt,
        days = emptyList()
    )
}