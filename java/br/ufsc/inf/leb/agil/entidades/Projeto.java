package br.ufsc.inf.leb.agil.entidades;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Projeto extends Documento {

	private static final long serialVersionUID = -3381122051102088378L;

	@JsonProperty private String nome;

	@JsonProperty private DateTime ultimaAtualizacao;

	@JsonProperty private String classePrincipal;

	@JsonProperty public TipoDeDocumento tipo = TipoDeDocumento.PROJETO;

	Projeto() {}

	public Projeto(String nome) {
		this.nome = nome;
		this.ultimaAtualizacao = new DateTime();
	}

	public void importarArquivos() {
		this.classePrincipal = null;
		this.ultimaAtualizacao = new DateTime();
	}

	public void fixarClassePrincipal(String classePrincipal) {
		this.classePrincipal = classePrincipal;
	}

	public void fixarNome(String nome) {
		this.nome = nome;
	}

	public String obterNome() {
		return nome;
	}

	public String obterClassePrincipal() {
		return classePrincipal;
	}

}
