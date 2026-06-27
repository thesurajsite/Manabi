package app.dev.manabi.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import app.dev.manabi.presentation.screens.MainScreen
import app.dev.manabi.presentation.screens.attendance.EditAttendanceMobileScreen

@Composable
fun ManabiNavHost(
    navState: ManabiNavState,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navState.navController,
        startDestination = Screen.MainGraph.Attendance,
        modifier = modifier
    ) {
        composable<Screen.MainGraph.Attendance> {
            MainScreen(
                currentScreen = Screen.MainGraph.Attendance,
                onNavigateToMain = navState::navigateToMain,
                onNavigateToEditAttendance = navState::navigateToEditAttendance,
            )
        }
        composable<Screen.MainGraph.Productivity> {
            MainScreen(
                currentScreen = Screen.MainGraph.Productivity,
                onNavigateToMain = navState::navigateToMain,
                onNavigateToEditAttendance = navState::navigateToEditAttendance,
            )
        }
        composable<Screen.MainGraph.Schedule> {
            MainScreen(
                currentScreen = Screen.MainGraph.Schedule,
                onNavigateToMain = navState::navigateToMain,
                onNavigateToEditAttendance = navState::navigateToEditAttendance,
            )
        }
        composable<Screen.EditAttendance> {
            EditAttendanceMobileScreen(
                onNavigateUp = { navState.navigateUp() }
            )
        }
    }
}
