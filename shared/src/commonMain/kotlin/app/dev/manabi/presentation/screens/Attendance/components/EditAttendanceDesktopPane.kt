package app.dev.manabi.presentation.screens.attendance.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.dev.manabi.domain.model.Attendance
import app.dev.manabi.presentation.screens.attendance.AttendanceViewModel
import app.dev.manabi.presentation.theme.screenBackground
import org.koin.compose.viewmodel.koinViewModel

@Suppress("ktlint:standard:function-naming")
@Composable
fun EditAttendanceDesktopPane(
    attendance: Attendance?,
    isMobile: Boolean,
    onBack: () -> Unit = {},
) {
    val viewModel: AttendanceViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    if (showDeleteConfirmation) {
        DeleteSubjectDialog(
            onConfirm = {
                showDeleteConfirmation = false
                viewModel.deleteAttendance(uiState.id, onBack)
            },
            onDismiss = { showDeleteConfirmation = false },
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(screenBackground),
    ) {
        EditAttendanceTopBar(
            title = if (attendance == null) "New subject" else "Edit subject",
            onClose = viewModel::closeSubject,
            onDelete = if (attendance != null) ({ showDeleteConfirmation = true }) else null,
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.TopCenter,
        ) {
            EditAttendanceContent(
                uiState = uiState,
                onSubjectNameChange = viewModel::updateSubjectName,
                onTeacherNameChange = viewModel::updateTeacherName,
                onRequirementChange = viewModel::updateRequirement,
                onSave = { viewModel.saveAttendance(onSuccess = {}) },
                onDayStatusChange = { date, status -> viewModel.setDayStatus(uiState.id, date, status) },
                modifier = Modifier
                    .widthIn(max = 640.dp)
                    .fillMaxSize(),
            )
        }
    }
}
