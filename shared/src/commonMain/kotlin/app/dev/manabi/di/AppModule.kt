package app.dev.manabi.di

import app.dev.manabi.data.repositoryImpl.AttendanceRepositoryImpl
import app.dev.manabi.data.source.local.SubjectLocalDataSource
import app.dev.manabi.database.ManabiDatabase
import app.dev.manabi.domain.repository.AttendanceRepository
import app.dev.manabi.domain.usecase.AddAttendanceUseCase
import app.dev.manabi.domain.usecase.GetAttendanceUseCase
import app.dev.manabi.presentation.screens.attendance.AttendanceViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformModule: Module

val appModule = module {
    single { 
        val database = ManabiDatabase(get())
        database.subjectQueries
    }
    singleOf(::SubjectLocalDataSource)

    // Repositories
    singleOf(::AttendanceRepositoryImpl) bind AttendanceRepository::class

    // UseCases
    singleOf(::AddAttendanceUseCase)
    singleOf(::GetAttendanceUseCase)

    // ViewModels
    viewModelOf(::AttendanceViewModel)
}
