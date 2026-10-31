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

        buildTypes.release.proguard {
            configurationFiles.from(project.file("compose-desktop.pro"))
        }

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
// Microsoft Store package (unsigned; the Store signs it): ./gradlew :desktopApp:packageMsix
// Needs makeappx.exe from the Windows SDK, or set MAKEAPPX to its path.
tasks.register<Exec>("packageMsix") {
    group = "compose desktop"
    description = "Builds the Microsoft Store .msix from the release app image."
    dependsOn("createReleaseDistributable")
    val version = compose.desktop.application.nativeDistributions.packageVersion
    commandLine(
        "powershell", "-NoProfile", "-ExecutionPolicy", "Bypass",
        "-File", file("msix/build-msix.ps1").absolutePath,
        "-AppDir", layout.buildDirectory.dir("compose/binaries/main-release/app/Manabi").get().asFile.absolutePath,
        "-Version", checkNotNull(version),
        "-OutFile", layout.buildDirectory.file("compose/binaries/main-release/msix/Manabi-$version.msix").get().asFile.absolutePath,
    )
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
