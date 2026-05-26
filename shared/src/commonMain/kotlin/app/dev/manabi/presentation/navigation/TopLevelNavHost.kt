package app.dev.manabi.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.dev.manabi.presentation.screens.Productivity.ProductivityScreen
import app.dev.manabi.presentation.screens.Schedule.ScheduleScreen
import app.dev.manabi.presentation.screens.attendance.AttendanceScreen

@Composable
fun TopLevelNavHost(
    currentScreen: Screen = Screen.MainGraph.Attendance,
    isMobile: Boolean,
    modifier: Modifier = Modifier
) {
    when (currentScreen) {
        Screen.MainGraph.Attendance -> AttendanceScreen(
            isMobile = isMobile,
            modifier = modifier
        )

        Screen.MainGraph.Productivity -> ProductivityScreen(
            isMobile = isMobile,
            modifier = modifier
        )

        Screen.MainGraph.Schedule -> ScheduleScreen(
            isMobile = isMobile,
            modifier = modifier
        )

    }
}