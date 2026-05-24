package app.dev.manabi.presentation.navigation

sealed interface MainDestination {

    val route: String
    val label: String

    data object Attendance : MainDestination {
        override val route: String = "attendance"
        override val label: String = "Attendance"
    }

    data object Productivity : MainDestination {
        override val route: String = "productivity"
        override val label: String = "Productivity"
    }

    data object Schedule : MainDestination {
        override val route: String = "schedule"
        override val label: String = "Schedule"
    }
}