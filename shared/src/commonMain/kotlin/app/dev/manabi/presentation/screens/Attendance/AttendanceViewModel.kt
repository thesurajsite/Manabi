package app.dev.manabi.presentation.screens.attendance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.dev.manabi.domain.model.Attendance
import app.dev.manabi.domain.model.AttendanceStatus
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
import kotlinx.datetime.*

@OptIn(kotlin.time.ExperimentalTime::class)
class AttendanceViewModel(
    private val addAttendanceUseCase: AddAttendanceUseCase,
    private val getAttendanceUseCase: GetAttendanceUseCase,
    private val deleteAttendanceUseCase: DeleteAttendanceUseCase,
) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedFilter = MutableStateFlow("All")
    val selectedFilter = _selectedFilter.asStateFlow()

    val attendanceList: StateFlow<List<Attendance>> =
        combine(getAttendanceUseCase(), _searchQuery, _selectedFilter) { list, query, filter ->
            var filteredList = if (query.isBlank()) {
                list
            } else {
                list.filter {
                    it.subjectName.contains(query, ignoreCase = true) ||
                        it.teacher.contains(query, ignoreCase = true)
                }
            }

            filteredList = when (filter) {
                "On Track" -> filteredList.filter { it.isOnTrack }
                "At Risk" -> filteredList.filter { !it.isOnTrack }
                "Can Miss" -> filteredList.filter { it.canSkip > 0 }
                "Must Go" -> filteredList.filter { it.classesNeeded > 0 }
                else -> filteredList
            }

            filteredList
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList(),
        )

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun updateFilter(filter: String) {
        _selectedFilter.value = filter
    }

    private val _isSubjectPaneOpen = MutableStateFlow(false)
    val isSubjectPaneOpen = _isSubjectPaneOpen.asStateFlow()

    private val _selectedSubjectId = MutableStateFlow<Long?>(null)
    val selectedSubjectId = _selectedSubjectId.asStateFlow()

    val selectedAttendance: StateFlow<Attendance?> =
        combine(getAttendanceUseCase(), _selectedSubjectId) { list, id ->
            list.find { it.id == id }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null,
        )

    private val _uiState = MutableStateFlow(AttendanceUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            selectedAttendance.collect { subject ->
                if (subject == null) return@collect
                _uiState.update {
                    it.copy(
                        conducted = subject.conducted,
                        attended = subject.attended,
                        days = subject.days,
                    )
                }
            }
        }
    }

    fun closeSubject() {
        _isSubjectPaneOpen.value = false
        _selectedSubjectId.value = null
    }

    fun openSubject(attendance: Attendance?) {
        _selectedSubjectId.value = attendance?.id
        _isSubjectPaneOpen.value = true

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
                // New subject starts with today marked present (1 conducted, 1 attended)
                AttendanceUiState(days = mapOf(currentDateString() to 1))
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

    fun deleteAttendance(
        id: Long,
        onSuccess: () -> Unit,
    ) {
        viewModelScope.launch {
            deleteAttendanceUseCase(id)
            closeSubject()
            onSuccess()
        }
    }

    /**
     * Get today's date in dd-MM-yyyy format.
     */
    private fun currentDateString(): String {
        val now = Clock.System.now()
        val timeZone = TimeZone.currentSystemDefault()
        val localDateTime = now.toLocalDateTime(timeZone)
        val monthNum = when (localDateTime.month) {
            Month.JANUARY -> 1
            Month.FEBRUARY -> 2
            Month.MARCH -> 3
            Month.APRIL -> 4
            Month.MAY -> 5
            Month.JUNE -> 6
            Month.JULY -> 7
            Month.AUGUST -> 8
            Month.SEPTEMBER -> 9
            Month.OCTOBER -> 10
            Month.NOVEMBER -> 11
            Month.DECEMBER -> 12
        }
        return "%02d-%02d-%04d".format(
            localDateTime.day,
            monthNum,
            localDateTime.year,
        )
    }

    fun todayStatusFor(subject: Attendance): AttendanceStatus = subject.statusFor(currentDateString())

    /**
     * Mark attendance as Present for the given subject and date.
     * Increments both conducted and attended counts (unless already present for that date).
     */
    fun markPresent(subjectId: Long, date: String = currentDateString()) {
        viewModelScope.launch {
            updateSubjectAttendance(subjectId, date, newStatus = 1)
        }
    }
    /**
     * Mark attendance as Absent for the given subject and date.
     * Increments conducted count only (unless already absent for that date).
     */
    fun markAbsent(subjectId: Long, date: String = currentDateString()) {
        viewModelScope.launch {
            updateSubjectAttendance(subjectId, date, newStatus = 0)
        }
    }

    /**
     * Remove the attendance entry for the given subject and date.
     * Decrements conducted, and attended too if the entry was present.
     */
    fun removeEntry(subjectId: Long, date: String) {
        viewModelScope.launch {
            updateSubjectAttendance(subjectId, date, newStatus = null)
        }
    }

    /** [newStatus]: 1 = present, 0 = absent, null = remove the entry. */
    private suspend fun updateSubjectAttendance(subjectId: Long, date: String, newStatus: Int?) {
        val currentList = getAttendanceUseCase().first()
        val subject = currentList.find { it.id == subjectId } ?: return

        val newDays = subject.days.toMutableMap()
        val previousStatus = newDays[date]
        if (previousStatus == newStatus) return

        if (newStatus == null) newDays.remove(date) else newDays[date] = newStatus

        var newConducted = subject.conducted
        var newAttended = subject.attended

        // Undo the previous entry
        when (previousStatus) {
            1 -> {
                newConducted--
                newAttended--
            }
            0 -> newConducted--
        }

        // Apply the new entry
        when (newStatus) {
            1 -> {
                newConducted++
                newAttended++
            }
            0 -> newConducted++
        }

        val now = Clock.System.now().toString()
        val updated = subject.copy(
            conducted = newConducted,
            attended = newAttended,
            days = newDays,
            updatedAt = now,
        )
        addAttendanceUseCase(updated)
    }
}

data class AttendanceUiState(
    val id: Long = 0,
    val subjectName: String = "",
    val conducted: Int = 1,
    val attended: Int = 1,
    val requirement: Int = 75,
    val teacher: String = "",
    val days: Map<String, Int> = emptyMap(),
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

    val canSkip: Int
        get() {
            if (percentage < requirement || requirement <= 0) return 0
            val r = requirement / 100f
            return max(0, ((attended.toFloat() / r) - conducted).toInt())
        }

    val isOnTrack: Boolean get() = percentage >= requirement
}
