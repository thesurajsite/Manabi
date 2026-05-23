package app.dev.manabi

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform