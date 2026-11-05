package app.dev.manabi.presentation.screens.Productivity

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.dev.manabi.presentation.screens.Productivity.components.DeleteDayDialog
import app.dev.manabi.presentation.screens.Productivity.components.ProductivityDatePicker
import app.dev.manabi.presentation.screens.Productivity.components.ProductivityDayDesktopPane
import app.dev.manabi.presentation.screens.Productivity.components.ProductivityListDesktopPane
import app.dev.manabi.presentation.theme.cardBorder
import org.koin.compose.viewmodel.koinViewModel

private val ListPaneWidth = 380.dp

@Composable
fun ProductivityDesktopScreen(modifier: Modifier) {
    val viewModel: ProductivityViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val selectedDate by viewModel.selectedDate.collectAsStateWithLifecycle()
    val draft by viewModel.draft.collectAsStateWithLifecycle()
    var isPickerOpen by remember { mutableStateOf(false) }
    var isDeleteDialogOpen by remember { mutableStateOf(false) }
    val activeDate = selectedDate ?: uiState.today
    val activeDay = uiState.dayFor(activeDate)

    Row(modifier = modifier.fillMaxSize()) {
        ProductivityListDesktopPane(
            uiState = uiState,
            activeDate = activeDate,
            onPlanDay = { isPickerOpen = true },
            onDayClick = viewModel::selectDate,
            modifier = Modifier.width(ListPaneWidth),
        )

        VerticalDivider(color = cardBorder, modifier = Modifier.fillMaxHeight())

        Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
            ProductivityDayDesktopPane(
                day = activeDay,
                draft = draft,
                onDraftChange = viewModel::updateDraft,
                onAddTask = { viewModel.addTask(activeDate) },
                onToggleTask = viewModel::toggleTask,
                onRemoveTask = viewModel::deleteTask,
                onChangeDate = { isPickerOpen = true },
                onDeleteDay = { isDeleteDialogOpen = true },
            )
        }
    }

    if (isDeleteDialogOpen) {
        DeleteDayDialog(
            day = activeDay,
            onConfirm = {
                isDeleteDialogOpen = false
                viewModel.deleteDay(activeDate)
            },
            onDismiss = { isDeleteDialogOpen = false },
        )
    }

    if (isPickerOpen) {
        ProductivityDatePicker(
            initialDate = activeDate,
            today = uiState.today,
            days = uiState.days,
            isWide = true,
            onDismiss = { isPickerOpen = false },
            onOpenDate = { date ->
                isPickerOpen = false
                viewModel.selectDate(date)
            },
        )
    }
}
