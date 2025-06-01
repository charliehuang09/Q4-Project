find . -name "*.java" -print0 | xargs -0 javac
java Server &
java Client &
java Client &
