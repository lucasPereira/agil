#!/bin/bash

pasta='/home/geral/projetos'
arquivoPid="${pasta}/construcao/projetos.pid"
executavel="${pasta}/sh/executar.sh"
logs="${pasta}/construcao/logs.txt"

case "$1" in
	start)
		echo "Iniciado Projetos."
		if [ ! -f ${arquivoPid} ]
		then
			cd ${pasta}
			bibliotecas=`find ${pasta}/jar -name "*.jar" | paste -s -d ":"`
			binarios="${pasta}/binarios"
			classePrincipal="br.ufsc.inf.leb.projetos.ServidorProjetos"
			classpath="${bibliotecas}:${binarios}"
			nohup java -classpath ${classpath} ${classePrincipal} >> ${logs} 2>> ${logs} & echo $! > ${arquivoPid}
		else
			echo "Serviço já está em execução"
		fi
	;;
	stop)
		echo "Parando Projetos."
		if [ -f ${arquivoPid} ]
		then
			kill -9 `cat ${arquivoPid}`
			rm -f ${arquivoPid}
		else
			echo "Servico já está parado"
		fi
	;;
	*)
		echo "Comando desconhecido."
	;;
esac
