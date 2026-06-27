package app.dev.manabi.domain.usecase

import app.dev.manabi.domain.model.Attendance
import app.dev.manabi.domain.repository.AttendanceRepository

class AddAttendanceUseCase(
    private val repository: AttendanceRepository
) {
    suspend operator fun invoke(attendance: Attendance) {
        repository.addAttendance(attendance)
    }
}
