package app.dev.manabi.presentation.screens.attendance.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.dev.manabi.presentation.screens.attendance.AttendanceUiState

/**
 * Scrollable body of the edit attendance screen, shared by mobile and desktop.
 * One column on mobile; with [twoColumns] (wide desktop pane) the cards sit on the left
 * and the calendar on the right.
 * Save applies the subject details and requirement; calendar changes save on their own.
 */
@Composable
fun EditAttendanceContent(
    uiState: AttendanceUiState,
    onSubjectNameChange: (String) -> Unit,
    onTeacherNameChange: (String) -> Unit,
    onRequirementChange: (Int) -> Unit,
    onSave: () -> Unit,
    onDayStatusChange: (date: String, status: Int?) -> Unit,
    modifier: Modifier = Modifier,
    twoColumns: Boolean = false,
) {
    val details: @Composable () -> Unit = {
        SubjectDetailsCard(
            subjectName = uiState.subjectName,
            teacherName = uiState.teacher,
            onSubjectNameChange = onSubjectNameChange,
            onTeacherNameChange = onTeacherNameChange,
        )
        AttendanceOverviewCard(
            percentage = uiState.percentage,
            requirement = uiState.requirement,
            isOnTrack = uiState.isOnTrack,
            canSkip = uiState.canSkip,
            classesNeeded = uiState.classesNeeded,
            attended = uiState.attended,
            missed = uiState.missed,
            conducted = uiState.conducted,
        )
        RequirementCard(
            requirement = uiState.requirement,
            percentage = uiState.percentage,
            isOnTrack = uiState.isOnTrack,
            onRequirementChange = onRequirementChange,
        )
        SaveSubjectButton(isNewSubject = uiState.isNewSubject, onClick = onSave)
    }
    val calendar: @Composable (Modifier) -> Unit = { calendarModifier ->
        AttendanceCalendar(
            days = uiState.days,
            isWide = twoColumns,
            // Only a saved subject can be marked
            onStatusChange = if (uiState.isNewSubject) null else onDayStatusChange,
            modifier = calendarModifier,
        )
    }

    if (twoColumns) {
        Row(
            modifier = modifier
                .verticalScroll(rememberScrollState())
                .padding(start = 32.dp, end = 32.dp, top = 4.dp, bottom = 32.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.Top,
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                details()
            }
            calendar(Modifier.weight(1.15f))
        }
    } else {
        Column(
            modifier = modifier
                .verticalScroll(rememberScrollState())
                .padding(start = 20.dp, end = 20.dp, top = 4.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            details()
            calendar(Modifier)
        }
    }
}
