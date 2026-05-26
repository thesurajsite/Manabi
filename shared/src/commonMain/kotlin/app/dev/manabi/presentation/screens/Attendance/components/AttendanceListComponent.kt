package app.dev.manabi.presentation.screens.attendance.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import app.dev.manabi.domain.model.Subject

@Composable
fun AttendanceListComponent(){

    val subjects = listOf(
        Subject(1, "Subject 1", 26, 20, 75, "25-june-26", "567", listOf("Monday", "Tuesday")),
        Subject(1, "Subject 1", 26, 20, 75, "25-june-26", "567", listOf("Monday", "Tuesday")),
        Subject(1, "Subject 1", 26, 20, 75, "25-june-26", "567", listOf("Monday", "Tuesday")),
        Subject(1, "Subject 1", 26, 20, 75, "25-june-26", "567", listOf("Monday", "Tuesday")),
        Subject(1, "Subject 1", 26, 20, 75, "25-june-26", "567", listOf("Monday", "Tuesday")),
    )

    LazyColumn {
        items(subjects){ subject->
            AttendanceItem()
        }
    }

}