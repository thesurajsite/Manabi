package app.dev.manabi.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

private val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE Subject ADD COLUMN days_json TEXT NOT NULL DEFAULT '{}'")
        database.execSQL("ALTER TABLE Subject DROP COLUMN days")
        database.execSQL("ALTER TABLE Subject RENAME COLUMN days_json TO days")
    }
}

fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<ManabiDatabase> {
    val appContext = context.applicationContext
    val dbFile = appContext.getDatabasePath("manabi.db")
    return Room.databaseBuilder<ManabiDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
        .fallbackToDestructiveMigration()
}
