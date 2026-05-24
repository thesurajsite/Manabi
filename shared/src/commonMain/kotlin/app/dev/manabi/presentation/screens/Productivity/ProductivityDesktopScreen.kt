package app.dev.manabi.presentation.screens.Productivity

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
fun ProductivityDesktopScreen(modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ScreenHeader(
            title = "Productivity",
            subtitle = "Desktop layout"
        )
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            InfoCard("Focus time", "4h 25m", Modifier.weight(1f))
            InfoCard("Task status", "8 completed, 2 remaining", Modifier.weight(1f))
            InfoCard("Trend", "Up 12% this week", Modifier.weight(1f))
        }
    }
}