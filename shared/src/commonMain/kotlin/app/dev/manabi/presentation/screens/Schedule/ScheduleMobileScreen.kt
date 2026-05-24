package app.dev.manabi.presentation.screens.Schedule

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.dev.manabi.presentation.components.InfoCard
import app.dev.manabi.presentation.components.ScreenHeader

@Composable
fun ScheduleMobileScreen(modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ScreenHeader(
            title = "Schedule",
            subtitle = "Mobile layout"
        )
        InfoCard("Today", "Math • 9:00 AM\nScience • 11:00 AM")
        InfoCard("Next class", "English • 2:00 PM")
    }
}