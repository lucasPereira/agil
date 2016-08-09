package br.ufsc.inf.leb.agil.restricoesDeInicializacao;

import java.io.File;

public class RestricaoDeArquivoExistente implements RestricaoDeInicializacao {

	private File arquivo;

	public RestricaoDeArquivoExistente(File arquivo) {
		this.arquivo = arquivo;
	}

	@Override
	public Boolean garantir() {
		Boolean existe = arquivo.exists();
		Boolean umArquivo = arquivo.isFile();
		return existe && umArquivo;
	}

	@Override
	public String obterDescricao() {
		return String.format("%s não é um arquivo ou não existe.", arquivo.getPath());
	}

}
