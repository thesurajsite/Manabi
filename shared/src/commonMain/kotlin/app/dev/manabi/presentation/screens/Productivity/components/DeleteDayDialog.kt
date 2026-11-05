package app.dev.manabi.presentation.screens.Productivity.components

import androidx.compose.runtime.Composable
import app.dev.manabi.domain.model.ProductivityDay
import app.dev.manabi.presentation.components.DeleteConfirmDialog
import app.dev.manabi.presentation.screens.Productivity.toWeekdayFullDate

@Composable
fun DeleteDayDialog(
    day: ProductivityDay,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    val tasks = if (day.totalCount == 1) "1 task" else "${day.totalCount} tasks"

    DeleteConfirmDialog(
        title = "Delete day",
        message = "Are you sure you want to delete ${day.date.toWeekdayFullDate()}? Its $tasks will be lost.",
        onConfirm = onConfirm,
        onDismiss = onDismiss,
    )
}
