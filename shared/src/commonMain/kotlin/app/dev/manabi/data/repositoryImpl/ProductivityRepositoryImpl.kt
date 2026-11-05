package app.dev.manabi.data.repositoryImpl

import app.dev.manabi.data.source.local.ProductivityLocalDataSource
import app.dev.manabi.database.ProductivityTaskEntity
import app.dev.manabi.domain.model.ProductivityTask
import app.dev.manabi.domain.repository.ProductivityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate

class ProductivityRepositoryImpl(
    private val localDataSource: ProductivityLocalDataSource,
) : ProductivityRepository {

    override fun getAllTasks(): Flow<List<ProductivityTask>> {
        return localDataSource.getTasks().map { tasks ->
            tasks.map {
                ProductivityTask(
                    id = it.id,
                    date = LocalDate.parse(it.date),
                    text = it.text,
                    isDone = it.isDone,
                    createdAt = it.createdAt,
                    updatedAt = it.updatedAt,
                )
            }
        }
    }

    override suspend fun addTask(task: ProductivityTask) {
        localDataSource.insertTask(
            ProductivityTaskEntity(
                date = task.date.toString(),
                text = task.text,
                isDone = task.isDone,
                createdAt = task.createdAt,
                updatedAt = task.updatedAt,
            )
        )
    }

    override suspend fun setTaskDone(id: Long, isDone: Boolean, updatedAt: String) {
        localDataSource.setTaskDone(id, isDone, updatedAt)
    }

    override suspend fun deleteTask(id: Long) {
        localDataSource.deleteTask(id)
    }
}
