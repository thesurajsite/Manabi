package app.dev.manabi.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.dev.manabi.domain.model.Attendance
import app.dev.manabi.presentation.screens.Productivity.ProductivityScreen
import app.dev.manabi.presentation.screens.Schedule.ScheduleScreen
import app.dev.manabi.presentation.screens.attendance.AttendanceScreen

@Composable
fun TopLevelNavHost(
    currentScreen: Screen,
    isMobile: Boolean,
    onNavigateToEditAttendance: (Attendance?) -> Unit,
    modifier: Modifier = Modifier
) {
    when (currentScreen) {
        Screen.MainGraph.Attendance -> AttendanceScreen(
            isMobile = isMobile,
            onNavigateToEditAttendance = onNavigateToEditAttendance,
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

        Screen.EditAttendance -> Unit
        else -> {}
    }
}