package br.ufsc.inf.leb.projetos.restricoesDeInicializacao;

import java.io.File;

public class RestricaoDePermissaoDeEscrita implements RestricaoDeInicializacao {

	private File arquivoOuDiretorio;

	public RestricaoDePermissaoDeEscrita(File arquivoOuDiretorio) {
		this.arquivoOuDiretorio = arquivoOuDiretorio;
	}

	@Override
	public Boolean garantir() {
		Boolean podeEscrever = arquivoOuDiretorio.canWrite();
		return podeEscrever;
	}

	@Override
	public String obterDescricao() {
		return String.format("%s não tem permissão de escrita.", arquivoOuDiretorio.getPath());
	}

}
