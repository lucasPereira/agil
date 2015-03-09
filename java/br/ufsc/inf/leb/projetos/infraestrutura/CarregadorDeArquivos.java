package br.ufsc.inf.leb.projetos.infraestrutura;

import java.io.File;

public class CarregadorDeArquivos {

	public File carregar(String pasta, String nome) {
		String caminho = String.format("./%s/%s", pasta, nome);
		return new File(caminho);
	}

}
