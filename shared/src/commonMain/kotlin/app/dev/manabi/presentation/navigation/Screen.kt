package app.dev.manabi.presentation.navigation

import app.dev.manabi.domain.model.Attendance
import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {

    @Serializable
    data object MainGraph {

        @Serializable
        data object Attendance : Screen()

        @Serializable
        data object Productivity : Screen()

        @Serializable
        data object Schedule : Screen()
    }

    @Serializable
    data class EditAttendance(val attendance: Attendance? = null) : Screen()
}
