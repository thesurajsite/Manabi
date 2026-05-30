package app.dev.manabi.presentation.screens.attendance

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AttendanceScreen(
    isMobile: Boolean,
    onNavigateToEditAttendance: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (isMobile) {
        AttendanceMobileScreen(
            onNavigateToEditAttendance = onNavigateToEditAttendance,
            modifier = modifier)
    } else {
        AttendanceDesktopScreen(modifier)
    }
}