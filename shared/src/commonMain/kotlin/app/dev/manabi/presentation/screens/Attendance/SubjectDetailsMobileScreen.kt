package app.dev.manabi.presentation.screens.attendance

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.dev.manabi.domain.model.Attendance
import app.dev.manabi.presentation.screens.attendance.components.SubjectDetailsPane
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SubjectDetailsMobileScreen(
    attendance: Attendance,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AttendanceViewModel = koinViewModel(),
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(attendance.subjectName) },
            )
        },
        modifier = modifier.fillMaxSize(),
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            SubjectDetailsPane(
                attendance = attendance,
                onPresentClick = { viewModel.markPresent(it.id) },
                onAbsentClick = { viewModel.markAbsent(it.id) },
            )
        }
    }
}
