package br.ufsc.inf.leb.agil;

import br.ufsc.inf.leb.agil.persistencia.BancoDeDocumentos;

public class AmbienteAgil {

	public BancoDeDocumentos obterBancoDeDocumentos() {
		return new BancoDeDocumentos(obterConfiguracoes());
	}

	public ConfiguracoesAgil obterConfiguracoes() {
		return new ConfiguracoesAgil();
	}

}
