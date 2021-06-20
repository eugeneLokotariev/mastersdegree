#!/bin/bash

TARGET_JAR_PATH=./target/blockchain-accounting-1.jar

if [ -f "$TARGET_JAR_PATH" ]
then
    java -jar "$TARGET_JAR_PATH";
else
    mvn clean install;
    java -jar "$TARGET_JAR_PATH";
fi
