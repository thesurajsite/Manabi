package app.dev.manabi.presentation.screens.attendance

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.dev.manabi.domain.model.Attendance
import app.dev.manabi.presentation.screens.attendance.components.DeleteSubjectDialog
import app.dev.manabi.presentation.screens.attendance.components.EditAttendanceContent
import app.dev.manabi.presentation.screens.attendance.components.EditAttendanceTopBar
import app.dev.manabi.presentation.theme.screenBackground
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EditAttendanceMobileScreen(
    attendance: Attendance?,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AttendanceViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    // Initialize the ViewModel state if it's the first time or if the attendance object changed
    LaunchedEffect(attendance) {
        viewModel.openSubject(attendance)
    }

    if (showDeleteConfirmation) {
        DeleteSubjectDialog(
            onConfirm = {
                showDeleteConfirmation = false
                viewModel.deleteAttendance(uiState.id, onNavigateUp)
            },
            onDismiss = { showDeleteConfirmation = false },
        )
    }

    Scaffold(
        containerColor = screenBackground,
        topBar = {
            EditAttendanceTopBar(
                title = if (attendance == null) "New subject" else "Edit subject",
                onBack = onNavigateUp,
                onDelete = if (attendance != null) ({ showDeleteConfirmation = true }) else null,
                modifier = Modifier.statusBarsPadding(),
            )
        },
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        EditAttendanceContent(
            uiState = uiState,
            onSubjectNameChange = viewModel::updateSubjectName,
            onTeacherNameChange = viewModel::updateTeacherName,
            onRequirementChange = viewModel::updateRequirement,
            onSave = { viewModel.saveAttendance(onSuccess = onNavigateUp) },
            onDayStatusChange = { date, status -> viewModel.setDayStatus(uiState.id, date, status) },
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
        )
    }
}
