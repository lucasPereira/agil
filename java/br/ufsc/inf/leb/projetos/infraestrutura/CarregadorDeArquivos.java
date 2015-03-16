package br.ufsc.inf.leb.projetos.infraestrutura;

import java.io.File;

import br.ufsc.inf.leb.projetos.AmbienteProjetos;

public class CarregadorDeArquivos {

	public File carregar(String pasta, String nome) {
		File pastaRaiz = new AmbienteProjetos().obterConfiguracoes().obterPastaRaiz();
		File pastaAtual = new File(pastaRaiz, pasta);
		File arquivo = new File(pastaAtual, nome);
		return arquivo;
	}

}
