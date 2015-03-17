#!/bin/bash

pasta=/home/geral/projetos
arquivoPid=/home/geral/projetos/construcao/projetos.pid
case $1 in
	start)
		echo "Iniciado Projetos."
		if [ ! -f $arquivoPid ]; then
			cd $pasta
			cd sh
			nohup executar.sh /tmp 2>> /dev/null >> /dev/null & echo $! > $arquivoPid
		else
			echo "Serviço já iniciado."
		fi
	stop)
		echo "Parando Projetos."
		if [ -f $arquivoPid ]; then
			pid=`cat $arquivoPid}`
			kill $pid
			rm $arquivoPid
		else 
			echo "Serviço já parado."i
		fi
	;;
esac
