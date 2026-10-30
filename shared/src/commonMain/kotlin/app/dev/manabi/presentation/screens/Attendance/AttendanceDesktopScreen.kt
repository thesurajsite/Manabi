package app.dev.manabi.presentation.screens.attendance

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.dev.manabi.presentation.screens.attendance.components.AttendanceListPane
import app.dev.manabi.presentation.screens.attendance.components.EditAttendanceDesktopPane
import app.dev.manabi.presentation.screens.attendance.components.NoSubjectDesktopPane
import app.dev.manabi.presentation.theme.cardBorder
import org.koin.compose.viewmodel.koinViewModel

private val ListPaneWidth = 380.dp

@Composable
fun AttendanceDesktopScreen(modifier: Modifier) {

    val viewmodel: AttendanceViewModel = koinViewModel()
    val isSubjectPaneOpen by viewmodel.isSubjectPaneOpen.collectAsStateWithLifecycle()
    val selectedAttendance by viewmodel.selectedAttendance.collectAsStateWithLifecycle()
    val selectedSubjectId by viewmodel.selectedSubjectId.collectAsStateWithLifecycle()
    val attendanceList by viewmodel.attendanceList.collectAsStateWithLifecycle()
    val searchQuery by viewmodel.searchQuery.collectAsStateWithLifecycle()
    val selectedFilter by viewmodel.selectedFilter.collectAsStateWithLifecycle()

    Row(modifier = modifier.fillMaxSize()) {
        // Attendance List Pane
        AttendanceListPane(
            attendanceList = attendanceList,
            searchQuery = searchQuery,
            selectedFilter = selectedFilter,
            selectedSubjectId = selectedSubjectId,
            todayStatusFor = viewmodel::todayStatusFor,
            onSearchQueryChange = viewmodel::updateSearchQuery,
            onFilterSelected = viewmodel::updateFilter,
            onItemClick = { attendance -> viewmodel.openSubject(attendance) },
            onChevronClick = { attendance -> viewmodel.openSubject(attendance) },
            onPresentClick = { attendance -> viewmodel.markPresent(attendance.id) },
            onAbsentClick = { attendance -> viewmodel.markAbsent(attendance.id) },
            onAddClick = { viewmodel.openSubject(null) },
            modifier = Modifier.width(ListPaneWidth),
        )

        VerticalDivider(color = cardBorder, modifier = Modifier.fillMaxHeight())

        // Attendance Detail Pane
        Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
            if (isSubjectPaneOpen) {
                EditAttendanceDesktopPane(
                    attendance = selectedAttendance,
                    isMobile = false,
                )
            } else {
                NoSubjectDesktopPane()
            }
        }
    }
}
