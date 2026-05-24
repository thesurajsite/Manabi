package app.dev.manabi.data.source.local

import app.dev.manabi.database.SubjectQueries

class SubjectLocalDataSource(
    private val queries: SubjectQueries
) {

    fun getSubjects() =
        queries.getAllSubjects().executeAsList()

    fun insertSubject(
        id: Long,
        subjectName: String,
        conducted: Long,
        attended: Long,
        requirement: Long,
        createdAt: String,
        updatedAt: String
    ) {
        queries.insertSubject(
            id = id,
            subjectName = subjectName,
            conducted = conducted,
            attended = attended,
            requirement = requirement,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}