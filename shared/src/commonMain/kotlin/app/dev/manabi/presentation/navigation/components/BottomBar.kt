package app.dev.manabi.presentation.navigation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.foundation.Image
import org.jetbrains.compose.resources.painterResource
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import app.dev.manabi.presentation.navigation.Screen
import app.dev.manabi.presentation.navigation.navigationItems

private val SelectedIconTint = Color(0xFF6C63FF)

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
            NavigationItems(
                currentScreen = currentScreen,
                onNavigate = onNavigate,
                showLabel = true,
            )
        }
    }
}

@Composable
fun NavigationItems(
    currentScreen: Screen,
    onNavigate: (Screen) -> Unit,
    showLabel: Boolean,
) {
    navigationItems.forEach { item ->
        val selected = currentScreen == item.screen

        TextButton(
            onClick = { onNavigate(item.screen) }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Show selected or unselected icon using Compose Multiplatform resources
                val icon = if (selected) item.selectedIcon else item.unselectedIcon
                Image(
                    painter = painterResource(icon),
                    contentDescription = item.label,
                    modifier = Modifier.size(24.dp),
                    colorFilter = if (selected) ColorFilter.tint(SelectedIconTint) else ColorFilter.tint(Color.Black),
                )

                if (showLabel) {
                    Text(
                        text = item.label,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                        color = if (selected) SelectedIconTint else Color.Black
                    )
                }
            }
        }
    }
}
