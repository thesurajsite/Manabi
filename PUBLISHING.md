# Publishing Manabi

Run every command from the project root.

## Before making any build

This applies to every platform: Android, iOS, Windows, Mac and Linux.

When someone asks for a build, ask these questions **before building**:

1. **Is this build for publishing or for testing?**
2. **Have you made any database changes?**
3. **Do you want to update the database version?**
4. **Do you want to update the app version?**

Then wait for the answers, and follow them:

- **Publishing:** also follow [Before every release](#before-every-release).
- **Database changes:** the database version needs raising, with a migration. See step 4 of the release checklist.
- **Updating versions:** make the change before building. The version locations are in step 3 of the release checklist.

## Before every release

1. **Start from `main`** and make sure it is up to date:

   ```bash
   git switch main
   git pull
   ```

2. **Run the tests:**

   ```bash
   ./gradlew :shared:jvmTest
   ```

3. **Raise the version.** Stores reject or won't deliver an update that reuses a version.

   | Platform | File | Values |
   |---|---|---|
   | Android | `androidApp/build.gradle.kts` | `versionCode` (add 1 every release), `versionName` |
   | iOS | `iosApp/Configuration/Config.xcconfig` | `CURRENT_PROJECT_VERSION`, `MARKETING_VERSION` |
   | Desktop | `desktopApp/build.gradle.kts` | `packageVersion` |

4. **Check the database.** If any entity or table changed:

   - Raise `version` in `shared/src/commonMain/kotlin/app/dev/manabi/database/ManabiDatabase.kt`.
   - Add a migration from the previous version, so user data is kept.
   - Commit the new schema file from `shared/schemas/`.
   - Extend `ManabiDatabaseMigrationTest`.

   All three platforms use `fallbackToDestructiveMigration`. If a migration is missing, the update **silently deletes the user's data**, with no error.

5. **Test an update, not only a fresh install.** Install the version that is currently live, add some data, install the new build over it, and confirm the data is still there.

## Android (Play Store)

The project has no release signing config, so sign the build from Android Studio:

1. Go to **Build > Generate Signed App Bundle / APK > Android App Bundle**.
2. Choose your upload keystore and the `release` build.
3. Upload the `.aab` to Play Console.

Keep the keystore and its passwords out of the repo.

## iOS (App Store)

1. Open `iosApp` in Xcode.
2. Choose **Any iOS Device** as the destination.
3. Go to **Product > Archive**.
4. Upload the archive to App Store Connect from the Organizer window.

## Windows (Microsoft Store)

This must be built on Windows, with the Windows SDK installed. The Store signs the package, so it is left unsigned here.

```bash
./gradlew :desktopApp:packageMsix
```

Output: `desktopApp/build/compose/binaries/main-release/msix/Manabi-<version>.msix`

`packageVersion` becomes the four-part Store version, for example `1.2.3` becomes `1.2.3.0`. Upload the `.msix` in Partner Center.

Identity values in `desktopApp/msix/AppxManifest.xml` must match Partner Center > Product management > Product identity.

## Mac

### Drag-and-drop installer

**Always build the Mac installer this way.** It creates the styled "Install Manabi" DMG, where the user drags Manabi onto the Applications folder.

Don't use `packageDmg`. That creates a plain DMG without this design.

```bash
./gradlew :desktopApp:packageStyledDmg
```

This must be built on a Mac. If macOS asks to let the build control Finder, allow it, because Finder sets the window layout.

Output:

```text
desktopApp/build/compose/binaries/main/styled-dmg/Manabi-<version>.dmg
```

The `build` folder is deleted by `./gradlew clean`, so copy the DMG somewhere else to keep it.

**What the installer contains:**

- `Manabi.app` on the left and an Applications shortcut on the right, sitting on the background's arrow.
- A 660x420 window using `desktopApp/dmg/background.tiff`, with 100-point icons.

To change the design, edit `desktopApp/dmg/background.tiff` or the layout values in `desktopApp/dmg/build-dmg.sh`. If you change the window size or the arrow position, update the icon positions in the script to match.

**After building, open the DMG and check how it looks:**

```bash
open desktopApp/build/compose/binaries/main/styled-dmg/Manabi-<version>.dmg
```

### Known gaps

- **Not Apple-signed:** the app only has an ad-hoc signature, so macOS Gatekeeper rejects it on other Macs. Public distribution needs an Apple Developer ID certificate and notarization, which aren't set up yet.
- **Custom disk icon:** `.VolumeIcon.icns` doesn't end up in the finished DMG, so the mounted disk may show the default icon.
- **Non-release build:** `packageStyledDmg` uses `createDistributable`, so it skips the ProGuard release setup.

## Linux

```bash
./gradlew :desktopApp:packageDeb
```

This must be built on Linux.

## After publishing

Tag the release, so you can always tell which code is live:

```bash
git tag v<version>
git push origin v<version>
```
