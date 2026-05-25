package app.dev.manabi.presentation.navigation.components

import androidx.compose.material3.TextButton
import androidx.compose.material3.Text
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.dev.manabi.presentation.navigation.Screen

@Composable
fun BottomBar(
    currentScreen: Screen,
    onNavigate: (Screen) -> Unit
) {
    NavigationBar {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            navigationItems(currentScreen, onNavigate)
        }
    }
}

@Composable
fun navigationItems(
    currentScreen: Screen,
    onNavigate: (Screen) -> Unit
) {
    val destinations = listOf(
        Screen.MainGraph.Attendance,
        Screen.MainGraph.Productivity,
        Screen.MainGraph.Schedule
    )

    destinations.forEach { destination ->
        val selected = currentScreen == destination

        TextButton(
            onClick = { onNavigate(destination) }
        ) {
            Text(
                text = destinationLabel(destination),
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}

private fun destinationLabel(destination: Screen): String = when (destination) {
    Screen.MainGraph.Attendance -> "Attendance"
    Screen.MainGraph.Productivity -> "Productivity"
    Screen.MainGraph.Schedule -> "Schedule"
    else -> "Attendance"
}
