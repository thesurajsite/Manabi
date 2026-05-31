package app.dev.manabi.presentation.navigation

import kotlinx.serialization.Serializable
import androidx.navigation3.runtime.NavKey

@Serializable
sealed class Screen : NavKey {

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
    data object EditAttendance : Screen()
}
