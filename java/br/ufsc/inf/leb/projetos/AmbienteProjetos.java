package br.ufsc.inf.leb.projetos;

import br.ufsc.inf.leb.projetos.persistencia.BancoDeDocumentos;

public class AmbienteProjetos {

	public BancoDeDocumentos obterBancoDeDocumentos() {
		return new BancoDeDocumentos(obterConfiguracoes());
	}

	public ConfiguracoesProjetos obterConfiguracoes() {
		return new ConfiguracoesProjetos();
	}

}
