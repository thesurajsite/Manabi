package app.dev.manabi.domain.repository

import app.dev.manabi.domain.model.Attendance

interface AttendanceRepository {
    suspend fun addAttendance(attendance: Attendance)
}
