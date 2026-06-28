package app.dev.manabi.presentation.screens.attendance

import androidx.lifecycle.ViewModel
import app.dev.manabi.domain.usecase.AddAttendanceUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AttendanceViewModel(
    private val addAttendanceUseCase: AddAttendanceUseCase
): ViewModel(){

    private val _showSubject = MutableStateFlow(false)
    val showSubject = _showSubject.asStateFlow()

    fun closeSubject(){
        _showSubject.value = false
    }

    fun openSubject(){
        _showSubject.value = true
    }

}
