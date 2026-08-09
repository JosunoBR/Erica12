@echo off
if not exist "app\src\main\res\mipmap-mdpi" mkdir "app\src\main\res\mipmap-mdpi"
if not exist "app\src\main\res\mipmap-hdpi" mkdir "app\src\main\res\mipmap-hdpi"
if not exist "app\src\main\res\mipmap-xhdpi" mkdir "app\src\main\res\mipmap-xhdpi"
if not exist "app\src\main\res\mipmap-xxhdpi" mkdir "app\src\main\res\mipmap-xxhdpi"
if not exist "app\src\main\res\mipmap-xxxhdpi" mkdir "app\src\main\res\mipmap-xxxhdpi"

copy /Y "app\src\main\ic_launcher.png" "app\src\main\res\mipmap-mdpi\ic_launcher.png"
copy /Y "app\src\main\ic_launcher.png" "app\src\main\res\mipmap-hdpi\ic_launcher.png"
copy /Y "app\src\main\ic_launcher.png" "app\src\main\res\mipmap-xhdpi\ic_launcher.png"
copy /Y "app\src\main\ic_launcher.png" "app\src\main\res\mipmap-xxhdpi\ic_launcher.png"
copy /Y "app\src\main\ic_launcher.png" "app\src\main\res\mipmap-xxxhdpi\ic_launcher.png"

copy /Y "app\src\main\ic_launcher.png" "app\src\main\res\mipmap-mdpi\ic_launcher_round.png"
copy /Y "app\src\main\ic_launcher.png" "app\src\main\res\mipmap-hdpi\ic_launcher_round.png"
copy /Y "app\src\main\ic_launcher.png" "app\src\main\res\mipmap-xhdpi\ic_launcher_round.png"
copy /Y "app\src\main\ic_launcher.png" "app\src\main\res\mipmap-xxhdpi\ic_launcher_round.png"
copy /Y "app\src\main\ic_launcher.png" "app\src\main\res\mipmap-xxxhdpi\ic_launcher_round.png"

copy /Y "app\src\main\ic_launcher.png" "app\src\main\res\drawable\ic_launcher.png"
