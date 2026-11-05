package app.dev.manabi.data.source.local

import app.dev.manabi.database.ProductivityDao
import app.dev.manabi.database.ProductivityTaskEntity
import kotlinx.coroutines.flow.Flow

class ProductivityLocalDataSource(
    private val productivityDao: ProductivityDao,
) {
    fun getTasks(): Flow<List<ProductivityTaskEntity>> =
        productivityDao.getAllTasks()

    suspend fun insertTask(task: ProductivityTaskEntity) {
        productivityDao.insertTask(task)
    }

    suspend fun setTaskDone(id: Long, isDone: Boolean, updatedAt: String) {
        productivityDao.setTaskDone(id, isDone, updatedAt)
    }

    suspend fun deleteTask(id: Long) {
        productivityDao.deleteTask(id)
    }
}
