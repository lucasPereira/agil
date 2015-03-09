package br.ufsc.inf.leb.projetos;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

public class ConfiguracoesProjetos {

	public URI obterUriBase() {
		return UriBuilder.fromUri("/").scheme("http").host("localhost").port(7000).build();
	}

	public URI coonstruirUri(Class<?> recurso, Object... parametros) {
		return UriBuilder.fromResource(recurso).scheme("http").host("localhost").port(7000).build(parametros);
	}

	public String obterNomeDaBaseDeDocumentos() {
		return "projetos";
	}

}
