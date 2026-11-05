package app.dev.manabi.presentation.screens.Productivity

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
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
import app.dev.manabi.presentation.screens.Productivity.components.ProductivityDayProgressCard
import app.dev.manabi.presentation.screens.Productivity.components.ProductivityDayTopBar
import app.dev.manabi.presentation.screens.Productivity.components.ProductivityTaskComposer
import app.dev.manabi.presentation.screens.Productivity.components.ProductivityTaskListCard
import app.dev.manabi.presentation.theme.screenBackground
import kotlinx.datetime.LocalDate
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProductivityDayMobileScreen(
    date: LocalDate,
    onNavigateUp: () -> Unit,
    onChangeDate: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProductivityViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val draft by viewModel.draft.collectAsStateWithLifecycle()
    var isPickerOpen by remember { mutableStateOf(false) }
    var isDeleteDialogOpen by remember { mutableStateOf(false) }
    val day = uiState.dayFor(date)

    Scaffold(
        containerColor = screenBackground,
        topBar = {
            ProductivityDayTopBar(
                title = date.toDayTitle(),
                onBack = onNavigateUp,
                onChangeDate = { isPickerOpen = true },
                onDeleteDay = if (day.tasks.isNotEmpty()) ({ isDeleteDialogOpen = true }) else null,
                modifier = Modifier.statusBarsPadding(),
            )
        },
        bottomBar = {
            ProductivityTaskComposer(
                draft = draft,
                onDraftChange = viewModel::updateDraft,
                onAdd = { viewModel.addTask(date) },
                isWide = false,
                modifier = Modifier
                    .navigationBarsPadding()
                    .imePadding(),
            )
        },
        modifier = modifier.fillMaxSize(),
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(start = 20.dp, end = 20.dp, top = 4.dp, bottom = 24.dp),
        ) {
            ProductivityDayProgressCard(day = day, isWide = false)
            ProductivityTaskListCard(
                day = day,
                onToggleTask = viewModel::toggleTask,
                onRemoveTask = viewModel::deleteTask,
            )
        }
    }

    if (isDeleteDialogOpen) {
        DeleteDayDialog(
            day = day,
            onConfirm = {
                isDeleteDialogOpen = false
                viewModel.deleteDay(date, onDeleted = onNavigateUp)
            },
            onDismiss = { isDeleteDialogOpen = false },
        )
    }

    if (isPickerOpen) {
        ProductivityDatePicker(
            initialDate = date,
            today = uiState.today,
            days = uiState.days,
            isWide = false,
            onDismiss = { isPickerOpen = false },
            onOpenDate = { picked ->
                isPickerOpen = false
                if (picked != date) onChangeDate(picked)
            },
        )
    }
}
