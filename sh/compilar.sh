#!/bin/bash

cd ..
mkdir -p binarios
bibliotecas=`find jar -name "*.jar" | paste -s -d ":"`
classes=`find java -name "*.java"`
javac -classpath $bibliotecas -d binarios $classes

