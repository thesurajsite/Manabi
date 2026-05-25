package app.dev.manabi.presentation.navigation

import manabi.shared.generated.resources.Attendance_filled
import manabi.shared.generated.resources.Productivity_filled
import manabi.shared.generated.resources.Res
import manabi.shared.generated.resources.Schedule_filled
import org.jetbrains.compose.resources.DrawableResource

data class NavigationItem(
    val screen: Screen,
    val label: String,
    val selectedIcon: DrawableResource,
    val unselectedIcon: DrawableResource,
)

val navigationItems = listOf(
    NavigationItem(
        screen = Screen.MainGraph.Attendance,
        label = "Attendance",
        selectedIcon = Res.drawable.Attendance_filled,
        unselectedIcon = Res.drawable.Attendance_filled,
    ),
    NavigationItem(
        screen = Screen.MainGraph.Productivity,
        label = "Productivity",
        selectedIcon = Res.drawable.Productivity_filled,
        unselectedIcon = Res.drawable.Productivity_filled,
    ),
    NavigationItem(
        screen = Screen.MainGraph.Schedule,
        label = "Schedule",
        selectedIcon = Res.drawable.Schedule_filled,
        unselectedIcon = Res.drawable.Schedule_filled,
    ),
)

