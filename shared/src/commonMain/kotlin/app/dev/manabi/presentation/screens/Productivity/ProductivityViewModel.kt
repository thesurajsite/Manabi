package app.dev.manabi.presentation.screens.Productivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.dev.manabi.domain.model.ProductivityDay
import app.dev.manabi.domain.model.ProductivitySummary
import app.dev.manabi.domain.model.ProductivityTask
import app.dev.manabi.domain.model.buildProductivitySummary
import app.dev.manabi.domain.model.toProductivityDays
import app.dev.manabi.domain.usecase.AddProductivityTaskUseCase
import app.dev.manabi.domain.usecase.DeleteProductivityTaskUseCase
import app.dev.manabi.domain.usecase.GetProductivityTasksUseCase
import app.dev.manabi.domain.usecase.SetProductivityTaskDoneUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.Duration.Companion.minutes

data class ProductivityUiState(
    val today: LocalDate,
    val days: List<ProductivityDay>,
    val summary: ProductivitySummary,
    val isLoading: Boolean = false,
) {
    /** The planned day for [date], or an empty day when it has no tasks. */
    fun dayFor(date: LocalDate): ProductivityDay =
        days.firstOrNull { it.date == date } ?: ProductivityDay(date, emptyList())
}

class ProductivityViewModel(
    getProductivityTasksUseCase: GetProductivityTasksUseCase,
    private val addProductivityTaskUseCase: AddProductivityTaskUseCase,
    private val setProductivityTaskDoneUseCase: SetProductivityTaskDoneUseCase,
    private val deleteProductivityTaskUseCase: DeleteProductivityTaskUseCase,
) : ViewModel() {

    // Restarts whenever the screen comes back, and re-checks each minute so a new day is picked up.
    private val today: Flow<LocalDate> = flow {
        while (true) {
            emit(currentDate())
            delay(1.minutes)
        }
    }.distinctUntilChanged()

    val uiState: StateFlow<ProductivityUiState> =
        combine(getProductivityTasksUseCase(), today) { tasks, today ->
            val days = toProductivityDays(tasks)
            ProductivityUiState(
                today = today,
                days = days,
                summary = buildProductivitySummary(days, today),
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = currentDate().let { date ->
                ProductivityUiState(
                    today = date,
                    days = emptyList(),
                    summary = buildProductivitySummary(emptyList(), date),
                    isLoading = true,
                )
            },
        )

    // Desktop only: null follows today. Mobile keeps the open date in its route.
    private val _selectedDate = MutableStateFlow<LocalDate?>(null)
    val selectedDate = _selectedDate.asStateFlow()

    private val _draft = MutableStateFlow("")
    val draft = _draft.asStateFlow()

    fun selectDate(date: LocalDate) {
        if (_selectedDate.value == date) return
        _selectedDate.value = date
        _draft.value = ""
    }

    fun updateDraft(text: String) {
        _draft.value = text
    }

    fun addTask(date: LocalDate) {
        val text = _draft.value.trim()
        if (text.isEmpty()) return
        _draft.value = ""

        val now = Clock.System.now().toString()
        viewModelScope.launch {
            addProductivityTaskUseCase(
                ProductivityTask(
                    id = 0,
                    date = date,
                    text = text,
                    isDone = false,
                    createdAt = now,
                    updatedAt = now,
                )
            )
        }
    }

    fun toggleTask(task: ProductivityTask) {
        val now = Clock.System.now().toString()
        viewModelScope.launch {
            setProductivityTaskDoneUseCase(task.id, !task.isDone, now)
        }
    }

    fun deleteTask(task: ProductivityTask) {
        viewModelScope.launch {
            deleteProductivityTaskUseCase(task.id)
        }
    }

    private fun currentDate(): LocalDate =
        Clock.System.todayIn(TimeZone.currentSystemDefault())
}
