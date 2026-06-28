package app.dev.manabi.domain.usecase

import app.dev.manabi.domain.repository.AttendanceRepository

class DeleteAttendanceUseCase(
    private val repository: AttendanceRepository
) {
    suspend operator fun invoke(id: Long) {
        repository.deleteAttendance(id)
    }
}
