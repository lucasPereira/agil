package br.ufsc.inf.leb.agil.entidades;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TipoDeDocumento {

	PROJETO("projeto");

	private String tipo;

	private TipoDeDocumento(String tipo) {
		this.tipo = tipo;
	}

	@Override
	@JsonValue
	public String toString() {
		return tipo;
	}

}
