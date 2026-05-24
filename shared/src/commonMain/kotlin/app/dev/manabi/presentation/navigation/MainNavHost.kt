package app.dev.manabi.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.dev.manabi.presentation.screens.Attendance.AttendanceScreen
import app.dev.manabi.presentation.screens.Productivity.ProductivityScreen
import app.dev.manabi.presentation.screens.Schedule.ScheduleScreen

@Composable
fun MainNavHost(
    currentRoute: String,
    isMobile: Boolean,
    modifier: Modifier = Modifier
) {

    when (currentRoute) {
        MainDestination.Productivity.route -> ProductivityScreen(
            isMobile = isMobile,
            modifier = modifier
        )

        MainDestination.Schedule.route -> ScheduleScreen(
            isMobile = isMobile,
            modifier = modifier
        )

        else -> AttendanceScreen(
            isMobile = isMobile,
            modifier = modifier
        )

    }
}