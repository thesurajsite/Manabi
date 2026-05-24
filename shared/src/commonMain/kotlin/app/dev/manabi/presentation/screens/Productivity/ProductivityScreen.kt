package app.dev.manabi.presentation.screens.Productivity

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ProductivityScreen(
    isMobile: Boolean,
    modifier: Modifier = Modifier
) {
    if (isMobile) {
        ProductivityMobileScreen(modifier)
    } else {
        ProductivityDesktopScreen(modifier)
    }
}