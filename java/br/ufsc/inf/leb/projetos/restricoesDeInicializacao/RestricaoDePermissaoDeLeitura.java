package br.ufsc.inf.leb.projetos.restricoesDeInicializacao;

import java.io.File;

public class RestricaoDePermissaoDeLeitura implements RestricaoDeInicializacao {

	private File arquivoOuDiretorio;

	public RestricaoDePermissaoDeLeitura(File arquivoOuDiretorio) {
		this.arquivoOuDiretorio = arquivoOuDiretorio;
	}

	@Override
	public Boolean garantir() {
		Boolean podeLer = arquivoOuDiretorio.canRead();
		return podeLer;
	}

	@Override
	public String obterDescricao() {
		return String.format("%s não tem permissão de leitura.", arquivoOuDiretorio.getPath());
	}

}
