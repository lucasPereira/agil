package br.ufsc.inf.leb.agil;

import java.io.File;
import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import br.ufsc.inf.leb.agil.dominio.Principal;
import br.ufsc.inf.leb.agil.restricoesDeInicializacao.AsseguradorDeRestricoesDeInicializacao;
import br.ufsc.inf.leb.agil.restricoesDeInicializacao.RestricaoDeDiretorioExistente;
import br.ufsc.inf.leb.agil.restricoesDeInicializacao.RestricaoDePermissaoDeEscrita;
import br.ufsc.inf.leb.agil.restricoesDeInicializacao.RestricaoDePermissaoDeLeitura;

public class ConfiguracoesAgil {

	ConfiguracoesAgil() {}

	public File obterPastaRaiz() {
		return new File(".");
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

	public File construirArquivo(String caminho) {
		return new File(obterPastaRaiz(), caminho);
	}

	public File obterDiretorioDosProjetos() {
		return construirArquivo("arquivosDosProjetos");
	}

	public File obterDiretorioDosProjetosTemporario() {
		return construirArquivo("arquivosTemporariosDosProjetos");
	}

	public File obterDiretorioDoProjeto(String nomeDoProjeto) {
		return new File(obterDiretorioDosProjetos(), nomeDoProjeto);
	}

	public File obterDiretorioDosArquivosDoProjeto(String nomeDoProjeto) {
		File diretorioDoProjeto = obterDiretorioDoProjeto(nomeDoProjeto);
		return new File(diretorioDoProjeto, "arquivos");
	}

	public File obterDiretorioDosArquivosDoProjetoTemporario(String nomeTemporarioDoProjeto) {
		return new File(obterDiretorioDosProjetosTemporario(), nomeTemporarioDoProjeto);
	}

	public File obterDiretorioDosBinariosDoProjeto(String nomeDoProjeto) {
		return new File(obterDiretorioDoProjeto(nomeDoProjeto), "bin");
	}

	public File obterDiretorioDeExecucaoDoProjeto(String nomeDoProjeto) {
		return new File(obterDiretorioDoProjeto(nomeDoProjeto), "execucao");
	}

	public File obterDiretorioDasBibliotecasDeExecucaoDoProjeto(String nomeDoProjeto) {
		return new File(obterDiretorioDeExecucaoDoProjeto(nomeDoProjeto), "libs");
	}

	public File obterArquivoDaBibliotecasDeExecucaoDoProjeto(String nomeDoProjeto, String nomeDaBiblioteca) {
		return new File(obterDiretorioDasBibliotecasDeExecucaoDoProjeto(nomeDoProjeto), nomeDaBiblioteca);
	}

	public File obterDiretorioDosFontesDoProjeto(String nomeDoProjeto) {
		return new File(obterDiretorioDosArquivosDoProjeto(nomeDoProjeto), "src");
	}

	public File obterDiretorioDasBibliotecasDoProjeto(String nomeDoProjeto) {
		return new File(obterDiretorioDosArquivosDoProjeto(nomeDoProjeto), "libs");
	}

	public File obterArquivoDoProjeto(String nomeDoProjeto, String caminho) {
		File diretorioDosArquivosDoProjeto = obterDiretorioDosArquivosDoProjeto(nomeDoProjeto);
		return new File(diretorioDosArquivosDoProjeto, caminho);
	}

	public File obterArquivoFonteDoProjeto(String nomeDoProjeto, String caminho) {
		File diretorioDosArquivosFontesDoProjeto = obterDiretorioDosFontesDoProjeto(nomeDoProjeto);
		return new File(diretorioDosArquivosFontesDoProjeto, caminho);
	}

	public File obterArquivoJarDoProjeto(String nomeDoProjeto) {
		File diretorioDeExecucaoDoProjeto = obterDiretorioDeExecucaoDoProjeto(nomeDoProjeto);
		return new File(diretorioDeExecucaoDoProjeto, obterNomeDeArquivoJar(nomeDoProjeto));
	}

	public File obterDireotrioDaClasseDoAppletDoProjeto(String nomeDoProjeto) {
		File diretorioDeExecucaoDoProjeto = obterDiretorioDeExecucaoDoProjeto(nomeDoProjeto);
		return new File(diretorioDeExecucaoDoProjeto, obterCaminhoDoArquivoClasseRelativoAoPacote());
	}

	public File obterArquivoDeExecucaoDoProjeto(String nomeDoProjeto, String arquivo) {
		File diretorioDeExecucaoDoProjeto = obterDiretorioDeExecucaoDoProjeto(nomeDoProjeto);
		return new File(diretorioDeExecucaoDoProjeto, arquivo);
	}

	public File obterArquivoDaClasseDoAppletDoProjeto(String nomeDoProjeto) {
		return new File(obterDireotrioDaClasseDoAppletDoProjeto(nomeDoProjeto), obterNomeDoArquivoClasseDoApplet());
	}

	public String obterNomeDoArquivoClasseDoApplet() {
		return String.format("%s.class", Principal.class.getSimpleName());
	}

	public File obterArquivoCompactadoDoProjeto(String nomeDoProjeto) {
		File diretorioDoProjeto = obterDiretorioDoProjeto(nomeDoProjeto);
		return new File(diretorioDoProjeto, obterNomeDeArquivoZip(nomeDoProjeto));
	}

	public File obterArquivoCompactadoDoProjetoTemporario(String nomeTemporarioDoProjeto) {
		return new File(obterDiretorioDosProjetosTemporario(), obterNomeDeArquivoZip(nomeTemporarioDoProjeto));
	}

	private String obterNomeDeArquivoZip(String prefixo) {
		return String.format("%s.zip", prefixo.replaceAll("/", "-"));
	}

	private String obterNomeDeArquivoJar(String prefixo) {
		return String.format("%s.jar", prefixo.replaceAll("/", "-"));
	}

	private String obterCaminhoDoArquivoClasseRelativoAoPacote() {
		return Principal.class.getPackage().getName().replaceAll("\\.", "/");
	}

	public String obterCaminhoDoArquivoFonteRealitivoAoDiretorioDosFontes(String nome) {
		return nome.replaceAll("\\.", "/") + ".java";
	}

	public String obterExpressaoRegularParaValidacaoDoNomeDoProjeto() {
		return "(ano2017/semestre1/)" + "[a-zA-Z0-9]([a-zA-Z0-9/])*[a-zA-Z0-9]";
	}

}
