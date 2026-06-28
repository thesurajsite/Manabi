package app.dev.manabi.data.source.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.dev.manabi.database.SubjectQueries
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow

class SubjectLocalDataSource(
    private val queries: SubjectQueries
) {
    fun getSubjects(): Flow<List<app.dev.manabi.database.Subject>> =
        queries.getAllSubjects().asFlow().mapToList(Dispatchers.IO)

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
