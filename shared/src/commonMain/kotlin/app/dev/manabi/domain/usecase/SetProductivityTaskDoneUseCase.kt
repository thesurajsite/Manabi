package app.dev.manabi.domain.usecase

import app.dev.manabi.domain.repository.ProductivityRepository

class SetProductivityTaskDoneUseCase(
    private val repository: ProductivityRepository,
) {
    suspend operator fun invoke(id: Long, isDone: Boolean, updatedAt: String) {
        repository.setTaskDone(id, isDone, updatedAt)
    }
}
