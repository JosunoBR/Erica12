"=== DIAGNOSTIC START ===" | Out-File -FilePath "log.txt" -Encoding utf8
"PATH: $env:PATH" | Out-File -FilePath "log.txt" -Append -Encoding utf8
"USERPROFILE: $env:USERPROFILE" | Out-File -FilePath "log.txt" -Append -Encoding utf8
Get-Command android -ErrorAction SilentlyContinue | Out-String | Out-File -FilePath "log.txt" -Append -Encoding utf8
Get-Command java -ErrorAction SilentlyContinue | Out-String | Out-File -FilePath "log.txt" -Append -Encoding utf8
"=== DIAGNOSTIC END ===" | Out-File -FilePath "log.txt" -Append -Encoding utf8
