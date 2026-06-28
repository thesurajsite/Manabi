// Provide a minimal, platform-agnostic nav graph for common code.
package app.dev.manabi

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import app.dev.manabi.di.appModule
import app.dev.manabi.di.platformModule
import app.dev.manabi.presentation.navigation.ManabiNavHost
import app.dev.manabi.presentation.navigation.rememberManabiNavState
import org.koin.compose.KoinApplication
import org.koin.dsl.KoinAppDeclaration

@Suppress("ktlint:standard:function-naming")
@Composable
fun App(koinConfiguration: KoinAppDeclaration = {}) {
    KoinApplication(application = {
        koinConfiguration()
        modules(appModule, platformModule)
    }) {
        val navState = rememberManabiNavState()

        MaterialTheme {
            ManabiNavHost(
                navState = navState,
            )
        }
    }
}
