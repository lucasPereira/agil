package br.ufsc.inf.leb.projetos.recursos;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import jersey.repackaged.com.google.common.collect.Lists;
import br.ufsc.inf.leb.projetos.AmbienteProjetos;
import br.ufsc.inf.leb.projetos.ConfiguracoesProjetos;
import br.ufsc.inf.leb.projetos.dominio.ExecutorDeComandos;
import br.ufsc.inf.leb.projetos.dominio.ManipuladorDeArquivos;
import br.ufsc.inf.leb.projetos.dominio.Principal;
import br.ufsc.inf.leb.projetos.entidades.ClassePrincipal;
import br.ufsc.inf.leb.projetos.entidades.Projeto;
import br.ufsc.inf.leb.projetos.persistencia.BancoDeDocumentos;
import br.ufsc.inf.leb.projetos.persistencia.RepositorioDeProjetos;

@Path("/projeto/{identificador}/classePrincipal")
public class RecursoProjetoClassePrincipal {

	@PUT
	@Consumes("application/json")
	public Response obter(@PathParam("identificador") String nomeDoProjeto, ClassePrincipal classePrincipal) throws IOException {
		AmbienteProjetos ambienteProjetos = new AmbienteProjetos();
		BancoDeDocumentos bancoDeDocumentos = ambienteProjetos.obterBancoDeDocumentos();
		RepositorioDeProjetos repositorioDeProjetos = bancoDeDocumentos.obterRepositorioDeProjetos();
		List<Projeto> projetos = repositorioDeProjetos.obterPorNome(nomeDoProjeto);
		if (projetos.size() > 1) {
			return Response.status(409).build();
		}
		if (projetos.size() < 1) {
			return Response.status(404).build();
		}
		ConfiguracoesProjetos configuracoes = ambienteProjetos.obterConfiguracoes();
		ManipuladorDeArquivos manipuladorDeArquivos = new ManipuladorDeArquivos();
		File arquivoFonteClassePrincipal = configuracoes.obterArquivoFonteDoProjeto(nomeDoProjeto, classePrincipal.obterCaminhoDaClasse());
		if (!arquivoFonteClassePrincipal.exists()) {
			return Response.status(404).build();
		}
		File diretorioDosBinarios = configuracoes.obterDiretorioDosBinariosDoProjeto(nomeDoProjeto);
		File diretorioDosFontes = configuracoes.obterDiretorioDosFontesDoProjeto(nomeDoProjeto);
		File diretorioDasBibliotecas = configuracoes.obterDiretorioDasBibliotecasDoProjeto(nomeDoProjeto);
		File diretorioDeExecucao = configuracoes.obterDiretorioDeExecucaoDoProjeto(nomeDoProjeto);
		File arquivoJar = configuracoes.obterArquivoJarDoProjeto(nomeDoProjeto);
		manipuladorDeArquivos.remover(diretorioDosBinarios);
		manipuladorDeArquivos.remover(diretorioDeExecucao);
		manipuladorDeArquivos.criarDiretorio(diretorioDosBinarios);
		manipuladorDeArquivos.criarDiretorio(diretorioDeExecucao);
		StringBuilder nomeDosArquivosFontesSeparadosPorEspaco = obterNomesDosArquivosFontesSeparadosPorClasse(configuracoes, diretorioDosFontes);
		try {
			ExecutorDeComandos executorDeComandos = new ExecutorDeComandos();
			Boolean compilou = executorDeComandos.executarComandoDeCompilacao(diretorioDosBinarios, diretorioDasBibliotecas, diretorioDosFontes, nomeDosArquivosFontesSeparadosPorEspaco);
			if (!compilou) {
				return Response.status(400).build();
			}
			Boolean compactou = executorDeComandos.executarComandoDeCompactacaoJar(diretorioDosBinarios, arquivoJar);
			if (!compactou) {
				return Response.status(500).build();
			}
			criarClassePrincipalDoApplet(nomeDoProjeto, manipuladorDeArquivos, configuracoes, diretorioDosBinarios);
			atualizarProjeto(classePrincipal, bancoDeDocumentos, projetos);
			return Response.status(200).build();
		} catch (InterruptedException excecao) {
			excecao.printStackTrace();
			return Response.status(400).build();
		}
	}

	private StringBuilder obterNomesDosArquivosFontesSeparadosPorClasse(ConfiguracoesProjetos configuracoes, File diretorioDosFontes) {
		StringBuilder nomeDosArquivosFontesSeparadosPorEspaco = new StringBuilder();
		List<File> arquivosFontes = obterArquivosFontes(configuracoes, diretorioDosFontes);
		Iterator<File> iterador = arquivosFontes.iterator();
		while (iterador.hasNext()) {
			nomeDosArquivosFontesSeparadosPorEspaco.append(iterador.next().getAbsolutePath());
			if (iterador.hasNext()) {
				nomeDosArquivosFontesSeparadosPorEspaco.append(" ");
			}
		}
		return nomeDosArquivosFontesSeparadosPorEspaco;
	}

	private List<File> obterArquivosFontes(ConfiguracoesProjetos configuracoes, File diretorioDosFontes) {
		List<File> arquivosFontes = Lists.newLinkedList();
		for (File arquivoFonte : diretorioDosFontes.listFiles()) {
			adicionarArquivosFontes(arquivosFontes, arquivoFonte);
		}
		return arquivosFontes;
	}

	private void adicionarArquivosFontes(List<File> arquivosFontes, File arquivoFonte) {
		if (arquivoFonte.isDirectory()) {
			for (File arquivoFilhoFonte : arquivoFonte.listFiles()) {
				adicionarArquivosFontes(arquivosFontes, arquivoFilhoFonte);
			}
		} else {
			arquivosFontes.add(arquivoFonte);
		}
	}

	private void atualizarProjeto(ClassePrincipal classePrincipal, BancoDeDocumentos bancoDeDocumentos, List<Projeto> projetos) {
		Projeto projeto = projetos.get(0);
		projeto.fixarClassePrincipal(classePrincipal.obterNome());
		bancoDeDocumentos.atualizarDocumento(projeto);
	}

	private void criarClassePrincipalDoApplet(String nomeDoProjeto, ManipuladorDeArquivos manipuladorDeArquivos, ConfiguracoesProjetos configuracoes, File diretorioDosBinarios) throws IOException {
		File diretorioDaClassePrincipalDoApplet = configuracoes.obterDireotrioDaClasseDoAppletDoProjeto(nomeDoProjeto);
		File arquivoDaClassePrinciaplDoApplet = configuracoes.obterArquivoDaClasseDoAppletDoProjeto(nomeDoProjeto);
		manipuladorDeArquivos.remover(diretorioDaClassePrincipalDoApplet);
		manipuladorDeArquivos.criarDiretorio(diretorioDaClassePrincipalDoApplet);
		manipuladorDeArquivos.remover(arquivoDaClassePrinciaplDoApplet);
		InputStream fluxoDeDadosDoArquivoDoApplet = Principal.class.getResourceAsStream(configuracoes.obterNomeDoArquivoClasseDoApplet());
		manipuladorDeArquivos.escreverArquivo(fluxoDeDadosDoArquivoDoApplet, arquivoDaClassePrinciaplDoApplet);
	}

}
