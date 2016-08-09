package br.ufsc.inf.leb.projetos.entidades;

import br.ufsc.inf.leb.projetos.AmbienteProjetos;

import com.fasterxml.jackson.annotation.JsonProperty;

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
		return new AmbienteProjetos().obterConfiguracoes().obterCaminhoDoArquivoFonteRealitivoAoDiretorioDosFontes(nome);
	}

}
