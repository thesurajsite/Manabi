package app.dev.manabi.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<ManabiDatabase> {
    val appContext = context.applicationContext
    val dbFile = appContext.getDatabasePath("manabi.db")
    return Room.databaseBuilder<ManabiDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}
