package br.ufsc.inf.leb.projetos;

import java.io.File;
import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import br.ufsc.inf.leb.projetos.restricoesDeInicializacao.AsseguradorDeRestricoesDeInicializacao;
import br.ufsc.inf.leb.projetos.restricoesDeInicializacao.RestricaoDeDiretorioExistente;
import br.ufsc.inf.leb.projetos.restricoesDeInicializacao.RestricaoDePermissaoDeEscrita;
import br.ufsc.inf.leb.projetos.restricoesDeInicializacao.RestricaoDePermissaoDeLeitura;

public class ConfiguracoesProjetos {

	ConfiguracoesProjetos() {}

	public File obterPastaRaiz() {
		return new File(".");
	}

	public File obterDiretorioDosArquivosDosProjetos() {
		return construirArquivo("arquivosDosProjetos");
	}

	public File obterDiretorioDosArquivosTemporariosDosProjetos() {
		return construirArquivo("arquivosTemporariosDosProjetos");
	}

	public File construirArquivo(String caminho) {
		return new File(obterPastaRaiz(), caminho);
	}

	public AsseguradorDeRestricoesDeInicializacao construirAsseguradorRestricoesDeInicializacao() {
		AsseguradorDeRestricoesDeInicializacao assegurador = new AsseguradorDeRestricoesDeInicializacao();
		assegurador.adicionarRestricao(new RestricaoDeDiretorioExistente(construirArquivo("css")));
		assegurador.adicionarRestricao(new RestricaoDeDiretorioExistente(construirArquivo("fonts")));
		assegurador.adicionarRestricao(new RestricaoDeDiretorioExistente(construirArquivo("html")));
		assegurador.adicionarRestricao(new RestricaoDeDiretorioExistente(construirArquivo("jar")));
		assegurador.adicionarRestricao(new RestricaoDeDiretorioExistente(construirArquivo("js")));
		assegurador.adicionarRestricao(new RestricaoDeDiretorioExistente(construirArquivo("arquivosDosProjetos")));
		assegurador.adicionarRestricao(new RestricaoDePermissaoDeLeitura(construirArquivo("arquivosDosProjetos")));
		assegurador.adicionarRestricao(new RestricaoDePermissaoDeEscrita(construirArquivo("arquivosDosProjetos")));
		assegurador.adicionarRestricao(new RestricaoDeDiretorioExistente(construirArquivo("arquivosTemporariosDosProjetos")));
		assegurador.adicionarRestricao(new RestricaoDePermissaoDeLeitura(construirArquivo("arquivosTemporariosDosProjetos")));
		assegurador.adicionarRestricao(new RestricaoDePermissaoDeEscrita(construirArquivo("arquivosTemporariosDosProjetos")));
		return assegurador;
	}

	public URI obterUriBase() {
		return UriBuilder.fromUri("/").scheme("http").host("localhost").port(7000).build();
	}

	public URI coonstruirUri(Class<?> recurso, Object... parametros) {
		return UriBuilder.fromResource(recurso).build(parametros);
	}

	public String obterNomeDaBaseDeDocumentos() {
		return "projetos";
	}

	public File obterDiretorioDosBinarios(File diretorioDoProjeto) {
		return new File(diretorioDoProjeto, "bin");
	}

	public String obterNomeDiretorioDosBinarios() {
		return "bin";
	}

	public String obterNomeDiretorioDosFontes() {
		return "src";
	}

}
