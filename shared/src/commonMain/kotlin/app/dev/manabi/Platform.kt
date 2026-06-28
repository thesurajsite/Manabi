package app.dev.manabi

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect fun showToast(message: String)
