// Provide a minimal, platform-agnostic nav graph for common code.
package app.dev.manabi

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import app.dev.manabi.di.appModule
import app.dev.manabi.di.platformModule
import app.dev.manabi.presentation.navigation.ManabiNavHost
import app.dev.manabi.presentation.navigation.rememberManabiNavState
import org.koin.compose.KoinApplication

@Composable
fun App() {
    KoinApplication(application = {
        modules(appModule, platformModule)
    }) {
        val navState = rememberManabiNavState()

        MaterialTheme {
            ManabiNavHost(
                navState = navState
            )
        }
    }
}
