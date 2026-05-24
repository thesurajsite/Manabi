package app.dev.manabi.presentation.screens.Schedule

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ScheduleScreen(
    isMobile: Boolean,
    modifier: Modifier = Modifier
) {
    if (isMobile) {
        ScheduleMobileScreen(modifier)
    } else {
        ScheduleDesktopScreen(modifier)
    }
}