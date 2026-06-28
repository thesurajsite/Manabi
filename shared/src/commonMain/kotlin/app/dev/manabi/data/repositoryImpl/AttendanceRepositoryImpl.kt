package app.dev.manabi.data.repositoryImpl

import app.dev.manabi.data.source.local.SubjectLocalDataSource
import app.dev.manabi.domain.model.Attendance
import app.dev.manabi.domain.repository.AttendanceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AttendanceRepositoryImpl(
    private val localDataSource: SubjectLocalDataSource
) : AttendanceRepository {

    override fun getAllAttendance(): Flow<List<Attendance>> {
        return localDataSource.getSubjects().map { subjects ->
            subjects.map { 
                Attendance(
                    id = it.id,
                    subjectName = it.subjectName,
                    conducted = it.conducted.toInt(),
                    attended = it.attended.toInt(),
                    requirement = it.requirement.toInt(),
                    days = it.days.split(",").filter { day -> day.isNotEmpty() },
                    teacher = it.teacher,
                    createdAt = it.createdAt,
                    updatedAt = it.updatedAt
                )
            }
        }
    }

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
