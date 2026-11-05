package app.dev.manabi.database

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import androidx.sqlite.execSQL
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.nio.file.Files
import kotlin.test.Test
import kotlin.test.assertEquals

class ManabiDatabaseMigrationTest {

    // Copied from schemas/.../2.json, so the seeded file looks like a real version 2 install.
    private val version2IdentityHash = "1f2ddb1e2542903da8a57fbb2f0b5ecb"

    @Test
    fun attendanceDataSurvivesTheUpgradeFromVersion2() = runBlocking {
        val dbFile = Files.createTempDirectory("manabi-migration").resolve("manabi.db").toFile()

        BundledSQLiteDriver().open(dbFile.absolutePath).use { connection ->
            connection.execSQL(
                "CREATE TABLE `Subject` (`id` INTEGER NOT NULL, `subjectName` TEXT NOT NULL, " +
                    "`conducted` INTEGER NOT NULL, `attended` INTEGER NOT NULL, `requirement` INTEGER NOT NULL, " +
                    "`days` TEXT NOT NULL, `teacher` TEXT NOT NULL, `createdAt` TEXT NOT NULL, " +
                    "`updatedAt` TEXT NOT NULL, PRIMARY KEY(`id`))"
            )
            connection.execSQL(
                "CREATE TABLE room_master_table (id INTEGER PRIMARY KEY, identity_hash TEXT)"
            )
            connection.execSQL(
                "INSERT INTO room_master_table (id, identity_hash) VALUES (42, '$version2IdentityHash')"
            )
            connection.execSQL(
                "INSERT INTO Subject VALUES (1, 'Networks', 10, 8, 75, '{}', 'Dr Rao', 'created', 'updated')"
            )
            connection.execSQL("PRAGMA user_version = 2")
        }

        // Same destructive fallback as the real builders: the migration must win over it.
        val database = getRoomDatabase(
            Room.databaseBuilder<ManabiDatabase>(
                name = dbFile.absolutePath,
                factory = { ManabiDatabaseConstructor.initialize() },
            ).fallbackToDestructiveMigration(dropAllTables = true)
        )

        val subjects = database.attendanceDao().getAllSubjects().first()
        assertEquals(listOf("Networks"), subjects.map { it.subjectName })
        assertEquals(8L, subjects.single().attended)

        database.productivityDao().insertTask(
            ProductivityTaskEntity(
                date = "2026-11-02",
                text = "Revise",
                isDone = false,
                createdAt = "created",
                updatedAt = "updated",
            )
        )
        assertEquals(listOf("Revise"), database.productivityDao().getAllTasks().first().map { it.text })

        database.close()
    }
}
