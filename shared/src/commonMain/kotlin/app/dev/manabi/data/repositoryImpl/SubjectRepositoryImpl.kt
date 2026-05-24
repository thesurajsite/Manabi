package app.dev.manabi.data.repositoryImpl

import app.dev.manabi.data.mapper.toDomain
import app.dev.manabi.data.model.Subject
import app.dev.manabi.data.source.local.SubjectLocalDataSource
import app.dev.manabi.domain.repository.SubjectRepository

class SubjectRepositoryImpl(
    private val localDataSource: SubjectLocalDataSource
) : SubjectRepository {

    override suspend fun getSubjects(): List<Subject> {
        return localDataSource
            .getSubjects()
            .map { it.toDomain() }
    }

    override suspend fun insertSubject(
        subject: Subject
    ) {
        localDataSource.insertSubject(
            id = subject.id,
            subjectName = subject.subjectName,
            conducted = subject.conducted.toLong(),
            attended = subject.attended.toLong(),
            requirement = subject.requirement.toLong(),
            createdAt = subject.createdAt,
            updatedAt = subject.updatedAt
        )
    }
}