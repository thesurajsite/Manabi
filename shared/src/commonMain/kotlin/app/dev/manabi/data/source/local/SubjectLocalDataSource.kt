package app.dev.manabi.data.source.local

import app.dev.manabi.database.AttendanceDao
import app.dev.manabi.database.AttendanceEntity
import kotlinx.coroutines.flow.Flow

class SubjectLocalDataSource(
    private val attendanceDao: AttendanceDao
) {
    fun getSubjects(): Flow<List<AttendanceEntity>> =
        attendanceDao.getAllSubjects()

    suspend fun insertSubject(
        id: Long,
        subjectName: String,
        conducted: Long,
        attended: Long,
        requirement: Long,
        days: Map<String, Int>,
        teacher: String,
        createdAt: String,
        updatedAt: String
    ) {
        attendanceDao.insertSubject(
            AttendanceEntity(
                id = id,
                subjectName = subjectName,
                conducted = conducted,
                attended = attended,
                requirement = requirement,
                days = days,
                teacher = teacher,
                createdAt = createdAt,
                updatedAt = updatedAt
            )
        )
    }

    suspend fun deleteSubject(id: Long) {
        attendanceDao.deleteSubject(id)
    }
}
