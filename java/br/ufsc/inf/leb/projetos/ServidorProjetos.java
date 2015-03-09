package br.ufsc.inf.leb.projetos;

import java.net.URI;

import org.glassfish.jersey.jetty.JettyHttpContainerFactory;

public class ServidorProjetos {

	public static void main(String[] argumentos) {
		AmbienteProjetos ambienteProjetos = new AmbienteProjetos();
		ConfiguracoesProjetos configuracoesProjetos = ambienteProjetos.obterConfiguracoes();
		URI uriBase = configuracoesProjetos.obterUriBase();
		AplicacaoProjetos aplicacaoProjetos = new AplicacaoProjetos();
		JettyHttpContainerFactory.createServer(uriBase, aplicacaoProjetos);
	}

}
