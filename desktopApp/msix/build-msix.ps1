# Packs the release app image into an unsigned .msix for Microsoft Store upload
# (the Store signs it). Run via: ./gradlew :desktopApp:packageMsix
param(
    [Parameter(Mandatory)] [string] $AppDir,
    [Parameter(Mandatory)] [string] $Version,
    [Parameter(Mandatory)] [string] $OutFile,
    [string] $MakeAppx = $env:MAKEAPPX
)
$ErrorActionPreference = 'Stop'
$here = $PSScriptRoot

if (-not $MakeAppx) {
    $MakeAppx = Get-ChildItem "${env:ProgramFiles(x86)}\Windows Kits\10\bin\*\x64\makeappx.exe" -ErrorAction SilentlyContinue |
        Sort-Object FullName -Descending | Select-Object -First 1 -ExpandProperty FullName
}
if (-not $MakeAppx -or -not (Test-Path $MakeAppx)) {
    throw "makeappx.exe not found. Install the Windows SDK or set MAKEAPPX to its path."
}

# Store packages need a four-part version with the last part 0 (1.2.3 -> 1.2.3.0).
$parts = @($Version.Split('.'))
while ($parts.Count -lt 3) { $parts += '0' }
$msixVersion = ($parts[0..2] + '0') -join '.'

New-Item -ItemType Directory -Force (Split-Path $OutFile) | Out-Null
$staging = Join-Path (Split-Path $OutFile) 'msix-staging'
if (Test-Path $staging) { Remove-Item -Recurse -Force $staging }
Copy-Item -Recurse $AppDir $staging

& "$here\make-icons.ps1" -Source "$here\..\src\main\resources\manabi.png" -OutDir "$staging\Assets"
(Get-Content -Raw "$here\AppxManifest.xml").Replace('${VERSION}', $msixVersion) |
    Set-Content -Encoding utf8 "$staging\AppxManifest.xml"

# resources.pri lets Windows pick the scale-/targetsize- qualified asset variants.
$makePri = Join-Path (Split-Path $MakeAppx) 'makepri.exe'
$priConfig = Join-Path (Split-Path $OutFile) 'priconfig.xml'
& $makePri createconfig /cf $priConfig /dq en-US /o | Out-Null
# Single package, no resource packs: keep every qualifier in one resources.pri.
(Get-Content -Raw $priConfig) -replace '(?s)<packaging>.*?</packaging>', '' | Set-Content -Encoding utf8 $priConfig
& $makePri new /pr $staging /cf $priConfig /mn "$staging\AppxManifest.xml" /of "$staging\resources.pri" /o | Out-Null
if ($LASTEXITCODE -ne 0) { throw "makepri failed with exit code $LASTEXITCODE" }

if (Test-Path $OutFile) { Remove-Item -Force $OutFile }
& $MakeAppx pack /o /h SHA256 /d $staging /p $OutFile
if ($LASTEXITCODE -ne 0) { throw "makeappx failed with exit code $LASTEXITCODE" }
Write-Host "MSIX written to $OutFile (version $msixVersion)"
