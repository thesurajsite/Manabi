package app.dev.manabi.presentation.screens.Attendance

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AttendanceScreen(
    isMobile: Boolean,
    modifier: Modifier = Modifier
) {
    if (isMobile) {
        AttendanceMobileScreen(modifier)
    } else {
        AttendanceDesktopScreen(modifier)
    }
}