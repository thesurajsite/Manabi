package app.dev.manabi.presentation.screens

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.dev.manabi.domain.model.Attendance
import app.dev.manabi.presentation.navigation.Screen
import app.dev.manabi.presentation.navigation.TopLevelNavHost
import app.dev.manabi.presentation.navigation.components.BottomBar
import app.dev.manabi.presentation.navigation.components.NavigationRailBar

@Composable
fun MainScreen(
    currentScreen: Screen,
    onNavigateToMain: (Screen) -> Unit,
    onNavigateToEditAttendance: (Attendance?) -> Unit
) {

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isMobile = maxWidth < 600.dp

        if (isMobile) {
            Scaffold(
                bottomBar = {
                    BottomBar(
                        currentScreen = currentScreen,
                        onNavigate = onNavigateToMain
                    )
                }
            ) { paddingValues ->

                TopLevelNavHost(
                    currentScreen = currentScreen,
                    isMobile = true,
                    onNavigateToEditAttendance = onNavigateToEditAttendance,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        } else {
            val railWidth = 60.dp

            Row(
                modifier = Modifier.fillMaxSize()
            ) {
                NavigationRailBar(
                    currentScreen = currentScreen,
                    onNavigate = onNavigateToMain,
                    modifier = Modifier.width(railWidth),
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    TopLevelNavHost(
                        currentScreen = currentScreen,
                        isMobile = false,
                        onNavigateToEditAttendance = onNavigateToEditAttendance,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}
