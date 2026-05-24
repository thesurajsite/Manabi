package app.dev.manabi.domain.usecase

import app.dev.manabi.data.model.Subject
import app.dev.manabi.domain.repository.SubjectRepository

class GetSubjectsUseCase(
    private val repository: SubjectRepository
) {
    suspend operator fun invoke(): List<Subject> {
        return repository.getSubjects()
    }
}