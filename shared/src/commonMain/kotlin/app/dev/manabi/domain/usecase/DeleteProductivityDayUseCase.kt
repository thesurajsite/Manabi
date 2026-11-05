package app.dev.manabi.domain.usecase

import app.dev.manabi.domain.repository.ProductivityRepository
import kotlinx.datetime.LocalDate

class DeleteProductivityDayUseCase(
    private val repository: ProductivityRepository,
) {
    suspend operator fun invoke(date: LocalDate) {
        repository.deleteDay(date)
    }
}
