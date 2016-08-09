package br.ufsc.inf.leb.projetos.restricoesDeInicializacao;

import java.io.File;

public class RestricaoDeDiretorioExistente implements RestricaoDeInicializacao {

	private File diretorio;

	public RestricaoDeDiretorioExistente(File diretorio) {
		this.diretorio = diretorio;
	}

	@Override
	public Boolean garantir() {
		Boolean existe = diretorio.exists();
		Boolean umDiretorio = diretorio.isDirectory();
		return existe && umDiretorio;
	}

	@Override
	public String obterDescricao() {
		return String.format("%s não é um diretório ou não existe.", diretorio.getPath());
	}

}
