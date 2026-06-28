package app.dev.manabi.domain.usecase

import app.dev.manabi.domain.model.Attendance
import app.dev.manabi.domain.repository.AttendanceRepository
import kotlinx.coroutines.flow.Flow

class GetAttendanceUseCase(
    private val repository: AttendanceRepository
) {
    operator fun invoke(): Flow<List<Attendance>> {
        return repository.getAllAttendance()
    }
}
