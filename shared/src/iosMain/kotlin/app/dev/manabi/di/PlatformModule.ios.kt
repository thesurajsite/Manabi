package app.dev.manabi.di

import androidx.room.RoomDatabase
import app.dev.manabi.database.ManabiDatabase
import app.dev.manabi.database.getDatabaseBuilder
import org.koin.dsl.module

actual val platformModule = module {
    single<RoomDatabase.Builder<ManabiDatabase>> { getDatabaseBuilder() }
}
