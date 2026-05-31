package app.dev.manabi.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import app.dev.manabi.presentation.screens.MainScreen
import app.dev.manabi.presentation.screens.attendance.EditAttendanceMobileScreen

@Composable
fun ManabiNavHost(
    navState: ManabiNavState,
    modifier: Modifier = Modifier,
) {
    val entryProvider = entryProvider<NavKey> {
        entry(Screen.MainGraph.Attendance) {
            MainScreen(
                currentScreen = Screen.MainGraph.Attendance,
                onNavigateToMain = navState::navigateToMain,
                onNavigateToEditAttendance = navState::navigateToEditAttendance,
            )
        }
        entry(Screen.MainGraph.Productivity) {
            MainScreen(
                currentScreen = Screen.MainGraph.Productivity,
                onNavigateToMain = navState::navigateToMain,
                onNavigateToEditAttendance = navState::navigateToEditAttendance,
            )
        }
        entry(Screen.MainGraph.Schedule) {
            MainScreen(
                currentScreen = Screen.MainGraph.Schedule,
                onNavigateToMain = navState::navigateToMain,
                onNavigateToEditAttendance = navState::navigateToEditAttendance,
            )
        }
        entry(Screen.EditAttendance) {
            EditAttendanceMobileScreen(
                onBack = { navState.popBackStack() }
            )
        }
    }

    NavDisplay(
        backStack = navState.backStack,
        modifier = modifier,
        onBack = { navState.popBackStack() },
        entryProvider = entryProvider
    )
}
