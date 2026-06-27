package app.dev.manabi.data.source.local

import app.dev.manabi.database.SubjectQueries

class SubjectLocalDataSource(
    private val queries: SubjectQueries
) {
    fun insertSubject(
        id: Long,
        subjectName: String,
        conducted: Long,
        attended: Long,
        requirement: Long,
        days: String,
        teacher: String,
        createdAt: String,
        updatedAt: String
    ) {
        queries.insertSubject(
            id = id,
            subjectName = subjectName,
            conducted = conducted,
            attended = attended,
            requirement = requirement,
            days = days,
            teacher = teacher,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}
