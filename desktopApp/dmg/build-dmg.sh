#!/bin/bash
# Builds a styled drag-and-drop DMG ("Install Manabi") from the app bundle
# produced by `./gradlew :desktopApp:createDistributable`.
#
# usage: build-dmg.sh <path/to/Manabi.app> <output.dmg>
set -euo pipefail

APP="$1"
OUT="$2"
DIR="$(cd "$(dirname "$0")" && pwd)"
VOLNAME="Manabi"
APP_NAME="$(basename "$APP")"

# Window content is 660x420 to match background.tiff; icons sit on the
# background's arrow (centers at x=165 / x=495, y=228).
WIN_W=660
WIN_H=420
ICON_SIZE=100

STAGE="$(mktemp -d)"
RW_DMG="$(mktemp -u).dmg"
trap 'hdiutil detach "/Volumes/$VOLNAME" -quiet 2>/dev/null || true; rm -rf "$STAGE" "$RW_DMG"' EXIT

# Unmount a leftover volume with the same name, or Finder styles the wrong one.
hdiutil detach "/Volumes/$VOLNAME" -quiet 2>/dev/null || true

cp -R "$APP" "$STAGE/"
ln -s /Applications "$STAGE/Applications"
mkdir "$STAGE/.background"
cp "$DIR/background.tiff" "$STAGE/.background/background.tiff"
cp "$DIR/../icons/manabi.icns" "$STAGE/.VolumeIcon.icns"

hdiutil create -quiet -srcfolder "$STAGE" -volname "$VOLNAME" -fs HFS+ \
    -format UDRW -size "$(( $(du -sm "$STAGE" | cut -f1) + 20 ))m" "$RW_DMG"
hdiutil attach -quiet -readwrite -noverify -noautoopen "$RW_DMG"

# Custom volume icon (needs Xcode command line tools; skipped otherwise).
if command -v SetFile >/dev/null; then
    SetFile -c icnC "/Volumes/$VOLNAME/.VolumeIcon.icns"
    SetFile -a C "/Volumes/$VOLNAME"
fi

osascript <<EOF
tell application "Finder"
    tell disk "$VOLNAME"
        open
        set current view of container window to icon view
        set toolbar visible of container window to false
        set statusbar visible of container window to false
        set the bounds of container window to {200, 120, $(( 200 + WIN_W )), $(( 120 + WIN_H + 28 ))}
        set opts to the icon view options of container window
        set arrangement of opts to not arranged
        set icon size of opts to $ICON_SIZE
        set text size of opts to 14
        set background picture of opts to file ".background:background.tiff"
        set position of item "$APP_NAME" of container window to {165, 228}
        set position of item "Applications" of container window to {495, 228}
        update without registering applications
        delay 2
        close
    end tell
end tell
EOF

chmod -Rf go-w "/Volumes/$VOLNAME" || true
sync
hdiutil detach -quiet "/Volumes/$VOLNAME"

mkdir -p "$(dirname "$OUT")"
rm -f "$OUT"
hdiutil convert -quiet "$RW_DMG" -format UDZO -imagekey zlib-level=9 -o "$OUT"
echo "Created $OUT"
