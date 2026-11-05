package app.dev.manabi.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductivityDao {
    @Query("SELECT * FROM ProductivityTask ORDER BY date DESC, id ASC")
    fun getAllTasks(): Flow<List<ProductivityTaskEntity>>

    @Insert
    suspend fun insertTask(task: ProductivityTaskEntity)

    @Query("UPDATE ProductivityTask SET isDone = :isDone, updatedAt = :updatedAt WHERE id = :id")
    suspend fun setTaskDone(id: Long, isDone: Boolean, updatedAt: String)

    @Query("DELETE FROM ProductivityTask WHERE id = :id")
    suspend fun deleteTask(id: Long)

    @Query("DELETE FROM ProductivityTask WHERE date = :date")
    suspend fun deleteTasksForDate(date: String)
}
