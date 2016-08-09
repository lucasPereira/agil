package br.ufsc.inf.leb.agil.entidades;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.ufsc.inf.leb.agil.AmbienteAgil;

public class ClassePrincipal {

	@JsonProperty
	private String nome;

	@JsonProperty
	private String caminho;

	public String obterNome() {
		return nome;
	}

	public String obterCaminho() {
		return caminho;
	}

	public String obterCaminhoDaClasse() {
		return new AmbienteAgil().obterConfiguracoes().obterCaminhoDoArquivoFonteRealitivoAoDiretorioDosFontes(nome);
	}

}
