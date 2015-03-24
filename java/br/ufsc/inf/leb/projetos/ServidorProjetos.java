package br.ufsc.inf.leb.projetos;

import java.net.URI;

import org.glassfish.jersey.jetty.JettyHttpContainerFactory;

import br.ufsc.inf.leb.projetos.restricoesDeInicializacao.AsseguradorDeRestricoesDeInicializacao;

public class ServidorProjetos {

	public static void main(String[] argumentos) {
		AmbienteProjetos ambienteProjetos = new AmbienteProjetos();
		ConfiguracoesProjetos configuracoesProjetos = ambienteProjetos.obterConfiguracoes();
		URI uriBase = configuracoesProjetos.obterUriBase();
		AsseguradorDeRestricoesDeInicializacao assegurador = configuracoesProjetos.construirAsseguradorRestricoesDeInicializacao();
		assegurador.garantir();
		AplicacaoProjetos aplicacaoProjetos = new AplicacaoProjetos();
		JettyHttpContainerFactory.createServer(uriBase, aplicacaoProjetos);
	}

}
