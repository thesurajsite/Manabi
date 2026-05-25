// Provide a minimal, platform-agnostic nav graph for common code.
package app.dev.manabi

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import app.dev.manabi.presentation.navigation.MainScreen

@Composable
@Preview
fun App() {
    // Simple, common-friendly nav "controller" — holds the current route
    val currentRoute = remember { mutableStateOf("main") }

    MaterialTheme {
        when (currentRoute.value) {

            "main" -> {
                // start destination is MainScreen
                MainScreen()
            }

            // additional routes may be added here
        }
    }
}