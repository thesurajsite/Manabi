package app.dev.manabi.presentation.navigation

import manabi.shared.generated.resources.attendance_filled
import manabi.shared.generated.resources.productivity_filled
import manabi.shared.generated.resources.Res
import manabi.shared.generated.resources.schedule_filled
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
        selectedIcon = Res.drawable.attendance_filled,
        unselectedIcon = Res.drawable.attendance_filled,
    ),
    NavigationItem(
        screen = Screen.MainGraph.Productivity,
        label = "Productivity",
        selectedIcon = Res.drawable.productivity_filled,
        unselectedIcon = Res.drawable.productivity_filled,
    ),
    NavigationItem(
        screen = Screen.MainGraph.Schedule,
        label = "Schedule",
        selectedIcon = Res.drawable.schedule_filled,
        unselectedIcon = Res.drawable.schedule_filled,
    ),
)

