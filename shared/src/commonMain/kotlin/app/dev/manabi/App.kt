// Provide a minimal, platform-agnostic nav graph for common code.
package app.dev.manabi

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import app.dev.manabi.presentation.screens.MainScreen
import app.dev.manabi.presentation.screens.attendance.EditAttendanceMobileScreen

@Composable
@Preview
fun App() {
    val navigationStack = remember { mutableStateOf(listOf("main")) }
    val currentRoute = navigationStack.value.last()

    MaterialTheme {
        when (currentRoute) {

            "main" -> {
                // start destination
                MainScreen(
                    onNavigateToEditAttendance = {
                        navigationStack.value += "editAttendance"
                    }
                )
            }

            "editAttendance" -> {
                BackHandler(onBack = {
                    if (navigationStack.value.size > 1) {
                        navigationStack.value = navigationStack.value.dropLast(1)
                    }
                })
                EditAttendanceMobileScreen(
                    onBack = {
                        if (navigationStack.value.size > 1) {
                            navigationStack.value = navigationStack.value.dropLast(1)
                        }
                    }
                )
            }

            // additional routes may be added here
        }
    }
}