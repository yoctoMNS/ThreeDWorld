#!/bin/bash
cd ..

javac -encoding utf8 -d ./bin -classpath ./res -sourcepath ./src ./src/com/github/yoctomns/threedworld/MainComponent.java

if [ $? -eq 0 ]; then
    java -classpath ./bin:./res com.github.yoctomns.threedworld.MainComponent
fi

cd src
