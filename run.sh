java Server &
SERVER_PID=$!
java Client
kill "$SERVER_PID"