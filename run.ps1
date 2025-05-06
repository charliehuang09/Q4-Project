javac (Get-ChildItem -Recurse -Filter *.java | Select-Object -ExpandProperty FullName)
Start-Process -NoNewWindow java Server
Start-Process -NoNewWindow java Client