package app.dev.manabi.domain.usecase

import app.dev.manabi.domain.model.ProductivityTask
import app.dev.manabi.domain.repository.ProductivityRepository
import kotlinx.coroutines.flow.Flow

class GetProductivityTasksUseCase(
    private val repository: ProductivityRepository,
) {
    operator fun invoke(): Flow<List<ProductivityTask>> {
        return repository.getAllTasks()
    }
}
