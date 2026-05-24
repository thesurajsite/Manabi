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

@Composable
fun MainScreen() {

    val currentRouteState = remember { mutableStateOf(MainDestination.Attendance.route) }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isMobile = maxWidth < 600.dp

        if (isMobile) {
            Scaffold(
                bottomBar = {
                    BottomBar(
                        currentRoute = currentRouteState.value,
                        onNavigate = { currentRouteState.value = it }
                    )
                }
            ) { paddingValues ->

                MainNavHost(
                    currentRoute = currentRouteState.value,
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
                    currentRoute = currentRouteState.value,
                    onNavigate = { currentRouteState.value = it }
                )
                Box(
                    modifier = Modifier
                        .width(contentWidth)
                        .fillMaxSize()
                ) {
                    MainNavHost(
                        currentRoute = currentRouteState.value,
                        isMobile = false,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}
