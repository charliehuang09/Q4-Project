mkdir -p bin
find . -name "*.java" -print0 | xargs -0 javac -d bin
cp -r sound bin/
cp -r img bin/
jar cfm Bonk.jar manifest.txt -C bin .