package app.dev.manabi.presentation.screens.Productivity

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.datetime.LocalDate

@Composable
fun ProductivityScreen(
    isMobile: Boolean,
    onNavigateToDay: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (isMobile) {
        ProductivityMobileScreen(
            onNavigateToDay = onNavigateToDay,
            modifier = modifier,
        )
    } else {
        ProductivityDesktopScreen(modifier)
    }
}
