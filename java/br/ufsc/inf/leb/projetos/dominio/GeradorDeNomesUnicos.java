package br.ufsc.inf.leb.projetos.dominio;

import java.util.Date;

public class GeradorDeNomesUnicos {

	public String gerar() {
		return String.format("%d", new Date().getTime());
	}

}
