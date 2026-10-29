package app.dev.manabi.presentation.screens.attendance

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.dev.manabi.domain.model.Attendance
import app.dev.manabi.presentation.screens.attendance.components.AttendanceListPane
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AttendanceMobileScreen(
    onNavigateToEditAttendance: (Attendance?) -> Unit,
    onNavigateToSubjectDetails: (Attendance) -> Unit,
    modifier: Modifier,
) {
    val viewModel: AttendanceViewModel = koinViewModel()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        AttendanceListPane(
            onItemClick = { },
            onChevronClick = { attendance ->
                onNavigateToEditAttendance(attendance)
            },
            onPresentClick = { attendance ->
                viewModel.markPresent(attendance.id)
            },
            onAbsentClick = { attendance ->
                viewModel.markAbsent(attendance.id)
            },
            onAddClick = {
                onNavigateToEditAttendance(null)
            },
        )
    }
}
