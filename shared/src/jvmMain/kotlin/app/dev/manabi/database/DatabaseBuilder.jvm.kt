package app.dev.manabi.database

import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.File

fun getDatabaseBuilder(): RoomDatabase.Builder<ManabiDatabase> {
    val dbFile = File(System.getProperty("java.io.tmpdir"), "manabi.db")
    return Room.databaseBuilder<ManabiDatabase>(
        name = dbFile.absolutePath,
        factory = { ManabiDatabaseConstructor.initialize() }
    )
}
