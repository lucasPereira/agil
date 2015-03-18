#!/bin/bash

cd $1
bibliotecas=`find $1/jar -name "*.jar" | paste -s -d ":"`
java -classpath $bibliotecas:$1/binarios br.ufsc.inf.leb.projetos.ServidorProjetos
