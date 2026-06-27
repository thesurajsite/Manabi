package app.dev.manabi.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

@Composable
fun rememberManabiNavState(): ManabiNavState {
    val module = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(Screen.MainGraph.Attendance::class, Screen.MainGraph.Attendance.serializer())
            subclass(Screen.MainGraph.Productivity::class, Screen.MainGraph.Productivity.serializer())
            subclass(Screen.MainGraph.Schedule::class, Screen.MainGraph.Schedule.serializer())
            subclass(Screen.EditAttendance::class, Screen.EditAttendance.serializer())
        }
    }
    val configuration = SavedStateConfiguration {
        serializersModule = module
    }
    val backStack = rememberNavBackStack(
        configuration = configuration,
        Screen.MainGraph.Attendance,
    )
    return remember(backStack) { ManabiNavState(backStack) }
}

class ManabiNavState internal constructor(
    val backStack: NavBackStack<NavKey>,
) {
    val currentDestination: Screen
        get() = backStack.last() as Screen

    fun navigateToMain(destination: Screen) {
        if (currentDestination == destination) return
        backStack.removeAt(backStack.size - 1)
        backStack.add(destination)
    }

    fun navigateToEditAttendance() {
        if (currentDestination == Screen.EditAttendance) return
        backStack.add(Screen.EditAttendance)
    }

    fun popBackStack(): Boolean {
        if (backStack.size <= 1) return false
        backStack.removeAt(backStack.size - 1)
        return true
    }
}
