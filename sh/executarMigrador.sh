#!/bin/bash

cd $1
migrador=MigradorRenomeadorDeArquivosZip
#migrador=MigradorRemovedorDePrefixoDosProjetos
#migrador=MigradorDeProjetosParaPastaEspecifica
bibliotecas=`find $1/jar -name "*.jar" | paste -s -d ":"`
java -classpath $bibliotecas:$1/binarios br.ufsc.inf.leb.agil.migrador.${migrador}
