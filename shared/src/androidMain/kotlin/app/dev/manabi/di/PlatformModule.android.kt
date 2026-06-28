package app.dev.manabi.di

import app.dev.manabi.database.DriverFactory
import org.koin.dsl.module

actual val platformModule = module {
    single { DriverFactory(get()).createDriver() }
}
