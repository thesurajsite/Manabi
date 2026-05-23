tasks.register("run") {
    group = "application"
    description = "Compatibility alias that delegates to :desktopApp:run"
    dependsOn(":desktopApp:run")
}

tasks.register("hotRun") {
    group = "application"
    description = "Compatibility alias that delegates to :desktopApp:hotRun"
    dependsOn(":desktopApp:hotRun")
}

tasks.register("build") {
    group = "build"
    description = "Compatibility alias that delegates to :desktopApp:build"
    dependsOn(":desktopApp:build")
}

