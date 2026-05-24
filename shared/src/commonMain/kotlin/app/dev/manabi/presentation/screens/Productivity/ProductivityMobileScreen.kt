package app.dev.manabi.presentation.screens.Productivity

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
fun ProductivityMobileScreen(modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ScreenHeader(
            title = "Productivity",
            subtitle = "Mobile layout"
        )
        InfoCard("Focus time", "4h 25m")
        InfoCard("Tasks completed", "8 out of 10")
    }
}