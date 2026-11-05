package app.dev.manabi.domain.usecase

import app.dev.manabi.domain.model.ProductivityTask
import app.dev.manabi.domain.repository.ProductivityRepository

class AddProductivityTaskUseCase(
    private val repository: ProductivityRepository,
) {
    suspend operator fun invoke(task: ProductivityTask) {
        repository.addTask(task)
    }
}
