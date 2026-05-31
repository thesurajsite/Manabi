// Provide a minimal, platform-agnostic nav graph for common code.
package app.dev.manabi

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import app.dev.manabi.presentation.navigation.ManabiNavHost
import app.dev.manabi.presentation.navigation.rememberManabiNavState

@Composable
@Preview
fun App() {
    val navState = rememberManabiNavState()

    MaterialTheme {
        ManabiNavHost(
            navState = navState
        )
    }
}