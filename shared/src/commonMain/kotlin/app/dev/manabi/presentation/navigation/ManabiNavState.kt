package app.dev.manabi.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import app.dev.manabi.domain.model.Attendance

@Composable
fun rememberManabiNavState(
    navController: NavHostController = rememberNavController()
): ManabiNavState = remember(navController) {
    ManabiNavState(navController)
}

class ManabiNavState(
    val navController: NavHostController,
) {
    fun navigateToMain(destination: Screen) {
        navController.navigate(destination) {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }
    }

    fun navigateToEditAttendance(attendance: Attendance? = null) {
        navController.navigate(Screen.EditAttendance(attendance))
    }

    fun navigateUp() {
        navController.navigateUp()
    }
}
