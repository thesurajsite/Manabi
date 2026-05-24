package app.dev.manabi.presentation.navigation

import androidx.compose.material3.NavigationRail
import androidx.compose.material3.TextButton
import androidx.compose.material3.Text
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun BottomBar(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {

    NavigationBar {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            navigationItems(currentRoute, onNavigate)
        }
    }
}

@Composable
fun navigationItems(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {

    val destinations = listOf(
        MainDestination.Attendance,
        MainDestination.Productivity,
        MainDestination.Schedule
    )

    destinations.forEach { destination ->
        val selected = currentRoute == destination.route

        TextButton(
            onClick = { onNavigate(destination.route) }
        ) {
            Text(
                text = destination.label,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}