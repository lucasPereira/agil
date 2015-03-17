#!/bin/bash

cd ..
bibliotecas=`find jar -name "*.jar" | paste -s -d ":"`
java -classpath $bibliotecas:binarios br.ufsc.inf.leb.projetos.ServidorProjetos
