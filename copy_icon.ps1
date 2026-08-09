$dirs = @(
    'app/src/main/res/mipmap-mdpi',
    'app/src/main/res/mipmap-hdpi',
    'app/src/main/res/mipmap-xhdpi',
    'app/src/main/res/mipmap-xxhdpi',
    'app/src/main/res/mipmap-xxxhdpi',
    'app/src/main/res/drawable'
)

foreach ($d in $dirs) {
    if (-not (Test-Path $d)) {
        New-Item -ItemType Directory -Path $d -Force
    }
}

$src = 'app/src/main/ic_launcher.png'
Copy-Item $src 'app/src/main/res/mipmap-mdpi/ic_launcher.png' -Force
Copy-Item $src 'app/src/main/res/mipmap-hdpi/ic_launcher.png' -Force
Copy-Item $src 'app/src/main/res/mipmap-xhdpi/ic_launcher.png' -Force
Copy-Item $src 'app/src/main/res/mipmap-xxhdpi/ic_launcher.png' -Force
Copy-Item $src 'app/src/main/res/mipmap-xxxhdpi/ic_launcher.png' -Force

Copy-Item $src 'app/src/main/res/mipmap-mdpi/ic_launcher_round.png' -Force
Copy-Item $src 'app/src/main/res/mipmap-hdpi/ic_launcher_round.png' -Force
Copy-Item $src 'app/src/main/res/mipmap-xhdpi/ic_launcher_round.png' -Force
Copy-Item $src 'app/src/main/res/mipmap-xxhdpi/ic_launcher_round.png' -Force
Copy-Item $src 'app/src/main/res/mipmap-xxxhdpi/ic_launcher_round.png' -Force

Copy-Item $src 'app/src/main/res/drawable/ic_launcher.png' -Force
Write-Output "Copied successfully"
