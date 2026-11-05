package app.dev.manabi.database

import androidx.room.Room
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.nio.file.Files
import kotlin.test.Test
import kotlin.test.assertEquals

class ProductivityDaoTest {

    private fun task(date: String, text: String) = ProductivityTaskEntity(
        date = date,
        text = text,
        isDone = false,
        createdAt = "created",
        updatedAt = "updated",
    )

    @Test
    fun deletingADayLeavesOtherDaysAlone() = runBlocking {
        val dbFile = Files.createTempDirectory("manabi-dao").resolve("manabi.db").toFile()
        val database = getRoomDatabase(
            Room.databaseBuilder<ManabiDatabase>(
                name = dbFile.absolutePath,
                factory = { ManabiDatabaseConstructor.initialize() },
            )
        )
        val dao = database.productivityDao()
        dao.insertTask(task("2026-11-05", "First"))
        dao.insertTask(task("2026-11-05", "Second"))
        dao.insertTask(task("2026-11-04", "Keep me"))

        dao.deleteTasksForDate("2026-11-05")

        assertEquals(listOf("Keep me"), dao.getAllTasks().first().map { it.text })

        database.close()
    }
}
