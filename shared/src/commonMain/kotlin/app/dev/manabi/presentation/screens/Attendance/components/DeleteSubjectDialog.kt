package app.dev.manabi.presentation.screens.attendance.components

import androidx.compose.runtime.Composable
import app.dev.manabi.presentation.components.DeleteConfirmDialog

@Composable
fun DeleteSubjectDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    DeleteConfirmDialog(
        title = "Delete subject",
        message = "Are you sure you want to delete this subject? Its attendance history will be lost.",
        onConfirm = onConfirm,
        onDismiss = onDismiss,
    )
}
