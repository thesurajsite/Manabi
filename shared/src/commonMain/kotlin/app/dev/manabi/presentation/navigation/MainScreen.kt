package app.dev.manabi.presentation.navigation

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.dev.manabi.presentation.navigation.components.BottomBar
import app.dev.manabi.presentation.navigation.components.NavigationRailBar

@Composable
fun MainScreen() {

    val currentScreenState = remember { mutableStateOf<Screen>(Screen.MainGraph.Attendance) }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isMobile = maxWidth < 600.dp

        if (isMobile) {
            Scaffold(
                bottomBar = {
                    BottomBar(
                        currentScreen = currentScreenState.value,
                        onNavigate = { currentScreenState.value = it }
                    )
                }
            ) { paddingValues ->

                TopLevelNavHost(
                    currentScreen = currentScreenState.value,
                    isMobile = true,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        } else {
            val railWidth = 96.dp
            val contentWidth = maxWidth - railWidth

            Row(
                modifier = Modifier.fillMaxSize()
            ) {
                NavigationRailBar(
                    currentScreen = currentScreenState.value,
                    onNavigate = { currentScreenState.value = it }
                )
                Box(
                    modifier = Modifier
                        .width(contentWidth)
                        .fillMaxSize()
                ) {
                    TopLevelNavHost(
                        currentScreen = currentScreenState.value,
                        isMobile = false,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}
