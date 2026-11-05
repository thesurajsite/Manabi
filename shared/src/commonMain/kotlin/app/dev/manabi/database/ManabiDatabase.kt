package app.dev.manabi.database

import androidx.room.AutoMigration
import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

@Database(
    entities = [AttendanceEntity::class, ProductivityTaskEntity::class],
    version = 3,
    autoMigrations = [AutoMigration(from = 2, to = 3)],
)
@TypeConverters(AttendanceConverters::class)
@ConstructedBy(ManabiDatabaseConstructor::class)
abstract class ManabiDatabase : RoomDatabase() {
    abstract fun attendanceDao(): AttendanceDao
    abstract fun productivityDao(): ProductivityDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT", "KotlinNoActualForExpect")
expect object ManabiDatabaseConstructor :
    RoomDatabaseConstructor<ManabiDatabase> {
    override fun initialize(): ManabiDatabase
}

fun getRoomDatabase(
    builder: RoomDatabase.Builder<ManabiDatabase>,
): ManabiDatabase {
    return builder
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}
