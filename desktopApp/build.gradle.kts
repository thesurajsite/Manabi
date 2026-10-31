import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

dependencies {
    implementation(projects.shared)

    implementation(compose.desktop.currentOs)
    implementation(libs.kotlinx.coroutinesSwing)

    implementation(libs.compose.uiToolingPreview)
}

compose.desktop {
    application {
        mainClass = "app.dev.manabi.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Manabi"
            packageVersion = "1.0.0"

            macOS {
                bundleID = "app.dev.manabi"
                iconFile.set(project.file("icons/manabi.icns"))
            }
            windows {
                iconFile.set(project.file("icons/manabi.ico"))
                shortcut = true
                menu = true
            }
            linux {
                packageName = "manabi"
                iconFile.set(project.file("icons/manabi.png"))
            }
        }
    }
}
// Styled drag-and-drop installer: ./gradlew :desktopApp:packageStyledDmg
tasks.register<Exec>("packageStyledDmg") {
    group = "compose desktop"
    description = "Builds the \"Install Manabi\" drag-to-Applications DMG."
    dependsOn("createDistributable")
    val version = compose.desktop.application.nativeDistributions.packageVersion
    commandLine(
        "bash",
        file("dmg/build-dmg.sh").absolutePath,
        layout.buildDirectory.file("compose/binaries/main/app/Manabi.app").get().asFile.absolutePath,
        layout.buildDirectory.file("compose/binaries/main/styled-dmg/Manabi-$version.dmg").get().asFile.absolutePath,
    )
}
