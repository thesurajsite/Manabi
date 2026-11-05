package app.dev.manabi.domain.usecase

import app.dev.manabi.domain.repository.ProductivityRepository

class DeleteProductivityTaskUseCase(
    private val repository: ProductivityRepository,
) {
    suspend operator fun invoke(id: Long) {
        repository.deleteTask(id)
    }
}
