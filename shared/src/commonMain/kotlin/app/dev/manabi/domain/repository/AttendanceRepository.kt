package app.dev.manabi.domain.repository

import app.dev.manabi.domain.model.Attendance
import kotlinx.coroutines.flow.Flow

interface AttendanceRepository {
    fun getAllAttendance(): Flow<List<Attendance>>
    suspend fun addAttendance(attendance: Attendance)
    suspend fun deleteAttendance(id: Long)
}
