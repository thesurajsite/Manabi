package app.dev.manabi.presentation.screens.Schedule

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
fun ScheduleDesktopScreen(modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ScreenHeader(
            title = "Schedule",
            subtitle = "Desktop layout"
        )
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            InfoCard("Today", "Math • 9:00 AM\nScience • 11:00 AM", Modifier.weight(1f))
            InfoCard("This week", "5 classes planned", Modifier.weight(1f))
            InfoCard("Reminder", "Assignment due tomorrow", Modifier.weight(1f))
        }
    }
}