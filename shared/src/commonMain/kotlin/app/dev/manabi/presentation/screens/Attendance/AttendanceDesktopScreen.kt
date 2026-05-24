package app.dev.manabi.presentation.screens.Attendance

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.dev.manabi.presentation.components.InfoCard
import app.dev.manabi.presentation.components.ScreenHeader

@Composable
fun AttendanceDesktopScreen(modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ScreenHeader(
            title = "Attendance",
            subtitle = "Desktop layout"
        )
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            InfoCard("Attendance summary", "Present: 24\nAbsent: 3", Modifier.weight(1f))
            InfoCard("Analytics", "Highest attendance day: Monday", Modifier.weight(1f))
        }
    }
}