package app.dev.manabi

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlin.system.exitProcess

fun main() = application {
    Window(
        onCloseRequest = { exitProcess(0) },
        title = "Manabi",
        icon = painterResource("manabi.png")
    ) {
        App()
    }
}