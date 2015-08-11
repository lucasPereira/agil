#!/bin/bash

cd $1
migrador=MigradorRemovedorDePrefixoDosProjetos
#migrador=MigradorDeProjetosParaPastaEspecifica
bibliotecas=`find $1/jar -name "*.jar" | paste -s -d ":"`
java -classpath $bibliotecas:$1/binarios br.ufsc.inf.leb.projetos.migrador.${migrador}
