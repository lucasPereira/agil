#!/bin/bash

confirmar=""

while [ "$confirmar" != "s" ] && [ "$confirmar" != "S" ] && [ "$confirmar" != "sim" ]
do
	read -p "Ano: " ano
	read -p "Semestre: " semestre
	read -p "Confirmar, sim (s) ou (*) não? " confirmar
	sed -i -r s/ano[0-9]+/ano${ano}/ ../html/projetosNovo.html
	sed -i -r s/ano[0-9]+/ano${ano}/ ../java/br/ufsc/inf/leb/agil/ConfiguracoesAgil.java
	sed -i -r s/ano[0-9]+/ano${ano}/ ../js/projetosNovo.js
	sed -i -r s/semestre[0-9]+/semestre${semestre}/ ../html/projetosNovo.html
	sed -i -r s/semestre[0-9]+/semestre${semestre}/ ../java/br/ufsc/inf/leb/agil/ConfiguracoesAgil.java
	sed -i -r s/semestre[0-9]+/semestre${semestre}/ ../js/projetosNovo.js
	git commit -m "Mudança para o ano ${ano} semestre ${semestre}."
	git tag -a ${ano}.${semestre} -m "Ano ${ano}. Semestre ${semestre}."
done

