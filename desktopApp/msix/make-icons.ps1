# Renders resized PNGs (and an optional multi-size .ico) from the master app icon.
param(
    [Parameter(Mandatory)] [string] $Source,
    [Parameter(Mandatory)] [string] $OutDir,
    [string] $IcoPath
)
$ErrorActionPreference = 'Stop'
Add-Type -AssemblyName System.Drawing

function Save-Png([System.Drawing.Image] $src, [int] $w, [int] $h, [string] $path, [double] $scale = 1.0) {
    $bmp = New-Object System.Drawing.Bitmap $w, $h
    $g = [System.Drawing.Graphics]::FromImage($bmp)
    $g.InterpolationMode = [System.Drawing.Drawing2D.InterpolationMode]::HighQualityBicubic
    $g.SmoothingMode = [System.Drawing.Drawing2D.SmoothingMode]::HighQuality
    $g.PixelOffsetMode = [System.Drawing.Drawing2D.PixelOffsetMode]::HighQuality
    $g.Clear([System.Drawing.Color]::Transparent)
    $side = [int]([Math]::Min($w, $h) * $scale)
    $g.DrawImage($src, [int](($w - $side) / 2), [int](($h - $side) / 2), $side, $side)
    $g.Dispose()
    $bmp.Save($path, [System.Drawing.Imaging.ImageFormat]::Png)
    $bmp.Dispose()
}

New-Item -ItemType Directory -Force $OutDir | Out-Null
$img = [System.Drawing.Image]::FromFile((Resolve-Path $Source))
try {
    # MSIX visual assets (scale-100 and scale-200, plus unplated taskbar icons).
    foreach ($s in @(1, 2)) {
        $q = "scale-$($s * 100)"
        Save-Png $img (44 * $s) (44 * $s) "$OutDir\Square44x44Logo.$q.png"
        Save-Png $img (150 * $s) (150 * $s) "$OutDir\Square150x150Logo.$q.png" 0.7
        Save-Png $img (310 * $s) (150 * $s) "$OutDir\Wide310x150Logo.$q.png" 0.7
        Save-Png $img (50 * $s) (50 * $s) "$OutDir\StoreLogo.$q.png"
    }
    foreach ($t in @(16, 24, 32, 48, 256)) {
        Save-Png $img $t $t "$OutDir\Square44x44Logo.targetsize-$t.png"
        Save-Png $img $t $t "$OutDir\Square44x44Logo.targetsize-$($t)_altform-unplated.png"
    }

    if ($IcoPath) {
        # PNG-compressed ICO entries (supported since Windows Vista).
        $sizes = @(16, 24, 32, 48, 64, 128, 256)
        $pngs = foreach ($t in $sizes) {
            $tmp = [System.IO.Path]::GetTempFileName()
            Save-Png $img $t $t $tmp
            , [System.IO.File]::ReadAllBytes($tmp)
            Remove-Item $tmp
        }
        $ms = New-Object System.IO.MemoryStream
        $bw = New-Object System.IO.BinaryWriter $ms
        $bw.Write([uint16]0); $bw.Write([uint16]1); $bw.Write([uint16]$sizes.Count)
        $offset = 6 + 16 * $sizes.Count
        for ($i = 0; $i -lt $sizes.Count; $i++) {
            $d = if ($sizes[$i] -ge 256) { 0 } else { $sizes[$i] }
            $bw.Write([byte]$d); $bw.Write([byte]$d); $bw.Write([byte]0); $bw.Write([byte]0)
            $bw.Write([uint16]1); $bw.Write([uint16]32)
            $bw.Write([uint32]$pngs[$i].Length); $bw.Write([uint32]$offset)
            $offset += $pngs[$i].Length
        }
        foreach ($p in $pngs) { $bw.Write($p) }
        $bw.Flush()
        New-Item -ItemType Directory -Force (Split-Path $IcoPath) | Out-Null
        [System.IO.File]::WriteAllBytes($IcoPath, $ms.ToArray())
        $bw.Dispose()
    }
} finally {
    $img.Dispose()
}
