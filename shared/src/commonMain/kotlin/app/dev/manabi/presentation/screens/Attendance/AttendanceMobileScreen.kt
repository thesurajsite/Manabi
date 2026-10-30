package app.dev.manabi.presentation.screens.attendance

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
    val attendanceList by viewModel.attendanceList.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedFilter by viewModel.selectedFilter.collectAsStateWithLifecycle()

    AttendanceListPane(
        attendanceList = attendanceList,
        searchQuery = searchQuery,
        selectedFilter = selectedFilter,
        todayStatusFor = viewModel::todayStatusFor,
        onSearchQueryChange = viewModel::updateSearchQuery,
        onFilterSelected = viewModel::updateFilter,
        onItemClick = { },
        onChevronClick = { attendance -> onNavigateToEditAttendance(attendance) },
        onPresentClick = { attendance -> viewModel.markPresent(attendance.id) },
        onAbsentClick = { attendance -> viewModel.markAbsent(attendance.id) },
        onAddClick = { onNavigateToEditAttendance(null) },
        modifier = modifier,
    )
}
