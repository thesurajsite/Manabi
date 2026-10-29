package app.dev.manabi.presentation.screens.attendance.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.dev.manabi.presentation.screens.attendance.AttendanceUiState
import app.dev.manabi.presentation.theme.primary

/**
 * Scrollable body of the edit attendance screen, shared by mobile and desktop.
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
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(start = 20.dp, end = 20.dp, top = 4.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
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

        Button(
            onClick = onSave,
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = primary, contentColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
        ) {
            Text(
                text = if (uiState.isNewSubject) "Add subject" else "Save changes",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )
        }

        AttendanceCalendar(
            days = uiState.days,
            // Only a saved subject can be marked
            onStatusChange = if (uiState.isNewSubject) null else onDayStatusChange,
        )
    }
}
