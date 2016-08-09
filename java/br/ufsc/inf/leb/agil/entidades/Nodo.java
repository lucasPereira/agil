package br.ufsc.inf.leb.agil.entidades;

import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.core.UriBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Nodo {

	@JsonProperty
	private String nome;

	@JsonProperty
	private List<Nodo> filhos;

	@JsonProperty
	private String conteudo;

	@JsonProperty
	private String caminho;

	@JsonProperty
	private Boolean diretorio;

	private Nodo(String nome, Boolean diretorio) {
		this.nome = nome;
		this.diretorio = diretorio;
		this.caminho = UriBuilder.fromUri(nome).build().toASCIIString();
		this.filhos = new LinkedList<Nodo>();
	}

	private Nodo(Nodo pai, String nome, Boolean diretorio) {
		this.nome = nome;
		this.diretorio = diretorio;
		this.caminho = UriBuilder.fromUri(pai.obterCaminho()).path(nome).build().toASCIIString();
		this.filhos = new LinkedList<Nodo>();
	}

	private String obterCaminho() {
		return caminho;
	}

	public static Nodo construirArquivo(String nome) {
		return new Nodo(nome, false);
	}

	public static Nodo construirDiretorio(String nome) {
		return new Nodo(nome, true);
	}

	public static Nodo construirArquivo(Nodo pai, String nome) {
		return new Nodo(pai, nome, false);
	}

	public static Nodo construirDiretorio(Nodo pai, String nome) {
		return new Nodo(pai, nome, true);
	}

	public void adicionarFilho(Nodo filho) {
		filhos.add(filho);
	}

	public void fixarConteudo(String conteudo) {
		this.conteudo = conteudo;
	}

}
