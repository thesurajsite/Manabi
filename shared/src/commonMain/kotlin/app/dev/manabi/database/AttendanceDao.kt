package app.dev.manabi.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AttendanceDao {
    @Query("SELECT * FROM Subject")
    fun getAllSubjects(): Flow<List<AttendanceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubject(subject: AttendanceEntity)

    @Query("DELETE FROM Subject WHERE id = :id")
    suspend fun deleteSubject(id: Long)

    @Query("DELETE FROM Subject")
    suspend fun deleteAllSubjects()
}
