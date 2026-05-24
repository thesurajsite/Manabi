package app.dev.manabi.domain.usecase

import app.dev.manabi.data.model.Subject
import app.dev.manabi.domain.repository.SubjectRepository

class AddSubjectUseCase(
    private val repository: SubjectRepository
) {
    suspend operator fun invoke(subject: Subject) {
        repository.addSubject(subject)
    }
}