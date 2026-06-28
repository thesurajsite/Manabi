package app.dev.manabi.presentation.screens.attendance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.dev.manabi.domain.model.Attendance
import app.dev.manabi.domain.usecase.AddAttendanceUseCase
import app.dev.manabi.domain.usecase.DeleteAttendanceUseCase
import app.dev.manabi.domain.usecase.GetAttendanceUseCase
import app.dev.manabi.showToast
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.roundToInt
import kotlin.time.Clock

@OptIn(kotlin.time.ExperimentalTime::class)
class AttendanceViewModel(
    private val addAttendanceUseCase: AddAttendanceUseCase,
    private val getAttendanceUseCase: GetAttendanceUseCase,
    private val deleteAttendanceUseCase: DeleteAttendanceUseCase,
) : ViewModel() {
    val attendanceList: StateFlow<List<Attendance>> = 
        getAttendanceUseCase()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    private val _showSubject = MutableStateFlow(false)
    val showSubject = _showSubject.asStateFlow()

    private val _selectedAttendance = MutableStateFlow<Attendance?>(null)
    val selectedAttendance = _selectedAttendance.asStateFlow()

    private val _uiState = MutableStateFlow(AttendanceUiState())
    val uiState = _uiState.asStateFlow()

    fun closeSubject() {
        _showSubject.value = false
        _selectedAttendance.value = null
    }

    fun openSubject(attendance: Attendance?) {
        _selectedAttendance.value = attendance
        _showSubject.value = true

        // Initialize UI state
        _uiState.value =
            if (attendance != null) {
                AttendanceUiState(
                    id = attendance.id,
                    subjectName = attendance.subjectName,
                    conducted = attendance.conducted,
                    attended = attendance.attended,
                    requirement = attendance.requirement,
                    teacher = attendance.teacher,
                    days = attendance.days,
                    createdAt = attendance.createdAt,
                )
            } else {
                AttendanceUiState()
            }
    }

    fun updateSubjectName(name: String) {
        _uiState.update { it.copy(subjectName = name) }
    }

    fun updateTeacherName(name: String) {
        _uiState.update { it.copy(teacher = name) }
    }

    fun updateRequirement(requirement: Int) {
        _uiState.update { it.copy(requirement = requirement.coerceIn(0, 100)) }
    }

    fun updateConducted(conducted: Int) {
        val newConducted = max(1, conducted)
        _uiState.update {
            it.copy(
                conducted = newConducted,
                attended = it.attended.coerceAtMost(newConducted),
            )
        }
    }

    fun updateAttended(attended: Int) {
        _uiState.update {
            it.copy(attended = attended.coerceIn(0, it.conducted))
        }
    }

    fun saveAttendance(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val state = _uiState.value

            if (state.subjectName.isBlank()) {
                showToast("Please enter a subject name")
                return@launch
            }

            val now = Clock.System.now().toString()
            val attendance =
                Attendance(
                    id = if (state.id == 0L) Clock.System.now().toEpochMilliseconds() else state.id,
                    subjectName = state.subjectName,
                    conducted = state.conducted,
                    attended = state.attended,
                    requirement = state.requirement,
                    days = state.days,
                    teacher = state.teacher,
                    createdAt = state.createdAt ?: now,
                    updatedAt = now,
                )
            addAttendanceUseCase(attendance)
            closeSubject()
            onSuccess()
        }
    }

    fun deleteAttendance(id: Long, onSuccess: () -> Unit) {
        viewModelScope.launch {
            deleteAttendanceUseCase(id)
            closeSubject()
            onSuccess()
        }
    }
}

data class AttendanceUiState(
    val id: Long = 0,
    val subjectName: String = "",
    val conducted: Int = 1,
    val attended: Int = 1,
    val requirement: Int = 75,
    val teacher: String = "",
    val days: List<String> = emptyList(),
    val createdAt: String? = null,
) {
    val percentage: Int
        get() = if (conducted == 0) 0 else ((attended.toFloat() / conducted) * 100).roundToInt()

    val classesNeeded: Int
        get() {
            if (percentage >= requirement) return 0
            val r = requirement / 100f
            return if (r >= 1f) 0 else max(0, ceil((r * conducted - attended) / (1 - r)).toInt())
        }

    val isOnTrack: Boolean get() = percentage >= requirement
}
