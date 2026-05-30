package app.dev.manabi.presentation.screens.attendance

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.dev.manabi.presentation.screens.attendance.components.AttendanceListPane

@Composable
fun AttendanceMobileScreen(
    onNavigateToEditAttendance: () -> Unit,
    modifier: Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AttendanceListPane(
            onSubjectClick = {
                onNavigateToEditAttendance()
            }
        )
    }
}