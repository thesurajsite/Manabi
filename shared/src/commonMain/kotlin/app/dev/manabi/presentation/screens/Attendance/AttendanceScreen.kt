package app.dev.manabi.presentation.screens.attendance

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.dev.manabi.domain.model.Attendance

@Composable
fun AttendanceScreen(
    isMobile: Boolean,
    onNavigateToEditAttendance: (Attendance?) -> Unit,
    onNavigateToSubjectDetails: (Attendance) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (isMobile) {
        AttendanceMobileScreen(
            onNavigateToEditAttendance = onNavigateToEditAttendance,
            onNavigateToSubjectDetails = onNavigateToSubjectDetails,
            modifier = modifier,
        )
    } else {
        AttendanceDesktopScreen(modifier)
    }
}
