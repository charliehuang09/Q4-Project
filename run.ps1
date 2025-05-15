javac (Get-ChildItem -Recurse -Filter *.java | Select-Object -ExpandProperty FullName)
Start-Process -NoNewWindow java Server
Start-Sleep -Seconds 1 # wait for server to start
java Client