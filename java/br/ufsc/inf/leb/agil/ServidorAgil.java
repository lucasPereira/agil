package br.ufsc.inf.leb.agil;

import java.net.URI;

import org.glassfish.jersey.jetty.JettyHttpContainerFactory;

import br.ufsc.inf.leb.agil.restricoesDeInicializacao.AsseguradorDeRestricoesDeInicializacao;

public class ServidorAgil {

	public static void main(String[] argumentos) {
		AmbienteAgil ambiente = new AmbienteAgil();
		ConfiguracoesAgil configuracoes = ambiente.obterConfiguracoes();
		URI uriBase = configuracoes.obterUriBase();
		AsseguradorDeRestricoesDeInicializacao assegurador = configuracoes.construirAsseguradorRestricoesDeInicializacao();
		assegurador.garantir();
		AplicacaoAgil aplicacao = new AplicacaoAgil();
		JettyHttpContainerFactory.createServer(uriBase, aplicacao);
	}

}
