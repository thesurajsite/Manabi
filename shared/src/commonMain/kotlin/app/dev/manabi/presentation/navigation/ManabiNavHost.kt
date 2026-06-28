package app.dev.manabi.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import androidx.savedstate.SavedState
import androidx.savedstate.read
import androidx.savedstate.write
import app.dev.manabi.domain.model.Attendance
import app.dev.manabi.presentation.screens.MainScreen
import app.dev.manabi.presentation.screens.attendance.EditAttendanceMobileScreen
import kotlinx.serialization.json.Json
import kotlin.reflect.typeOf

// Custom NavType to handle the Attendance object in the route
val AttendanceNavType = object : NavType<Attendance?>(isNullableAllowed = true) {
    override fun get(bundle: SavedState, key: String): Attendance? {
        return bundle.read { getString(key) }?.let { Json.decodeFromString(it) }
    }

    override fun parseValue(value: String): Attendance? {
        return if (value == "null") null else Json.decodeFromString(value)
    }

    override fun put(bundle: SavedState, key: String, value: Attendance?) {
        bundle.write { putString(key, Json.encodeToString(value)) }
    }

    override fun serializeAsValue(value: Attendance?): String {
        return if (value == null) "null" else Json.encodeToString(value)
    }
}

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
        composable<Screen.EditAttendance>(
            typeMap = mapOf(typeOf<Attendance?>() to AttendanceNavType)
        ) { backStackEntry ->
            val destination: Screen.EditAttendance = backStackEntry.toRoute()
            EditAttendanceMobileScreen(
                attendance = destination.attendance,
                onNavigateUp = { navState.navigateUp() }
            )
        }
    }
}
