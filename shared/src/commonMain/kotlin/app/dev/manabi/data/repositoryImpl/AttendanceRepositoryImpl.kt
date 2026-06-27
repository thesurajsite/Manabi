package app.dev.manabi.data.repositoryImpl

import app.dev.manabi.data.source.local.SubjectLocalDataSource
import app.dev.manabi.domain.model.Attendance
import app.dev.manabi.domain.repository.AttendanceRepository

class AttendanceRepositoryImpl(
    private val localDataSource: SubjectLocalDataSource
) : AttendanceRepository {

    override suspend fun addAttendance(attendance: Attendance) {
        localDataSource.insertSubject(
            id = attendance.id,
            subjectName = attendance.subjectName,
            conducted = attendance.conducted.toLong(),
            attended = attendance.attended.toLong(),
            requirement = attendance.requirement.toLong(),
            days = attendance.days.joinToString(","),
            teacher = attendance.teacher,
            createdAt = attendance.createdAt,
            updatedAt = attendance.updatedAt
        )
    }
}
