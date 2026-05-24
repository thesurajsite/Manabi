package app.dev.manabi.domain.repository

import app.dev.manabi.data.model.Subject

interface SubjectRepository {

    suspend fun getSubjects(): List<Subject>

    suspend fun insertSubject(
        subject: Subject
    )
}