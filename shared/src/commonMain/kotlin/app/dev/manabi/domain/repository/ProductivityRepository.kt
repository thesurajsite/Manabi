package app.dev.manabi.domain.repository

import app.dev.manabi.domain.model.ProductivityTask
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface ProductivityRepository {
    fun getAllTasks(): Flow<List<ProductivityTask>>
    suspend fun addTask(task: ProductivityTask)
    suspend fun setTaskDone(id: Long, isDone: Boolean, updatedAt: String)
    suspend fun deleteTask(id: Long)
    suspend fun deleteDay(date: LocalDate)
}
