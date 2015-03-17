package br.ufsc.inf.leb.projetos.recursos;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import jersey.repackaged.com.google.common.collect.Lists;
import br.ufsc.inf.leb.projetos.AmbienteProjetos;
import br.ufsc.inf.leb.projetos.ConfiguracoesProjetos;
import br.ufsc.inf.leb.projetos.dominio.Principal;
import br.ufsc.inf.leb.projetos.entidades.ClassePrincipal;
import br.ufsc.inf.leb.projetos.entidades.Projeto;
import br.ufsc.inf.leb.projetos.infraestrutura.ManipuladorDeArquivos;
import br.ufsc.inf.leb.projetos.persistencia.BancoDeDocumentos;
import br.ufsc.inf.leb.projetos.persistencia.RepositorioDeProjetos;

@Path("/projeto/{identificador}/classePrincipal")
public class RecursoProjetoClassePrincipal {

	@PUT
	@Consumes("application/json")
	public Response obter(@PathParam("identificador") String identificador, ClassePrincipal classePrincipal) throws IOException {
		AmbienteProjetos ambienteProjetos = new AmbienteProjetos();
		BancoDeDocumentos bancoDeDocumentos = ambienteProjetos.obterBancoDeDocumentos();
		RepositorioDeProjetos repositorioDeProjetos = bancoDeDocumentos.obterRepositorioDeProjetos();
		List<Projeto> projetos = repositorioDeProjetos.obterPorNome(identificador);
		if (projetos.size() > 1) {
			return Response.status(409).build();
		}
		if (projetos.size() < 1) {
			return Response.status(404).build();
		}
		ConfiguracoesProjetos configuracoes = ambienteProjetos.obterConfiguracoes();
		File diretorioDosProjetos = configuracoes.obterDiretorioDosArquivosDosProjetos();
		File diretorioDoProjeto = new File(diretorioDosProjetos, identificador);
		File arquivoClassePrincipal = new File(diretorioDosProjetos, classePrincipal.obterCaminho());
		if (!arquivoClassePrincipal.exists()) {
			return Response.status(404).build();
		}
		ManipuladorDeArquivos manipuladorDeArquivos = new ManipuladorDeArquivos();
		File diretorioDosBinarios = criarDiretorioDosBinarios(configuracoes, diretorioDoProjeto, manipuladorDeArquivos);
		File diretorioDosBinariosCompilados = new File(diretorioDosBinarios, "class");
		diretorioDosBinariosCompilados.mkdir();
		File diretorioDosFontes = new File(diretorioDoProjeto, configuracoes.obterNomeDiretorioDosFontes());
		File diretorioDasBibliotecas = new File(diretorioDoProjeto, configuracoes.obterNomeDiretorioDasBibliotecas());
		String nomeDoArquivoJarCompilado = identificador + ".jar";
		File arquivoDoJarCompilado = new File(diretorioDosBinarios, nomeDoArquivoJarCompilado);
		StringBuilder nomeDosArquivosFontesSeparadosPorEspaco = obterNomesDosArquivosFontesSeparadosPorClasse(configuracoes, diretorioDoProjeto, diretorioDosFontes);
		String comandoDeCompilacao = String.format("javac -d %s -classpath %s/* -sourcepath %s %s", diretorioDosBinariosCompilados.getAbsolutePath(), diretorioDasBibliotecas.getAbsolutePath(), diretorioDosFontes.getAbsolutePath(), nomeDosArquivosFontesSeparadosPorEspaco.toString());
		try {
			Process processoDeCompilacao = Runtime.getRuntime().exec(comandoDeCompilacao);
			processoDeCompilacao.waitFor(10, TimeUnit.SECONDS);
			System.out.println(comandoDeCompilacao);
			System.out.println(processoDeCompilacao.exitValue());
			if (!processoDeCompilacao.isAlive() && processoDeCompilacao.exitValue() == 0) {
				manipuladorDeArquivos.escreverNaSaidaPadrao(processoDeCompilacao.getInputStream());
				String comandoDeCompactacao= String.format("jar cf %s -C %s .", arquivoDoJarCompilado.getAbsolutePath(), diretorioDosBinariosCompilados.getAbsolutePath());
				Process processoDeCompactacao = Runtime.getRuntime().exec(comandoDeCompactacao);
				processoDeCompactacao.waitFor(10, TimeUnit.SECONDS);
				System.out.println(comandoDeCompactacao);
				System.out.println(processoDeCompactacao.exitValue());
				if (!processoDeCompactacao.isAlive() && processoDeCompactacao.exitValue() == 0) {
					manipuladorDeArquivos.escreverNaSaidaPadrao(processoDeCompactacao.getInputStream());
					criarClassePrincipalDoApplet(manipuladorDeArquivos, diretorioDosBinarios);
					atualizarProjeto(classePrincipal, bancoDeDocumentos, projetos);
					return Response.status(200).build();
				} else {
					return Response.status(400).type("text/plain").entity(processoDeCompactacao.getErrorStream()).build();
				}
			} else {
				return Response.status(400).type("text/plain").entity(processoDeCompilacao.getErrorStream()).build();
			}
		} catch (InterruptedException excecao) {
			excecao.printStackTrace();
			return Response.status(400).build();
		}
	}

	private StringBuilder obterNomesDosArquivosFontesSeparadosPorClasse(ConfiguracoesProjetos configuracoes, File diretorioDoProjeto, File diretorioDosFontes) {
		StringBuilder nomeDosArquivosFontesSeparadosPorEspaco = new StringBuilder();
		List<File> arquivosFontes = obterArquivosFontes(configuracoes, diretorioDoProjeto, diretorioDosFontes);
		Iterator<File> iterador = arquivosFontes.iterator();
		while (iterador.hasNext()) {
			nomeDosArquivosFontesSeparadosPorEspaco.append(iterador.next().getAbsolutePath());
			if (iterador.hasNext()) {
				nomeDosArquivosFontesSeparadosPorEspaco.append(" ");
			}
		}
		return nomeDosArquivosFontesSeparadosPorEspaco;
	}

	private List<File> obterArquivosFontes(ConfiguracoesProjetos configuracoes, File diretorioDoProjeto, File diretorioDosFontes) {
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

	private void criarClassePrincipalDoApplet(ManipuladorDeArquivos manipuladorDeArquivos, File diretorioDosBinarios) throws IOException {
		File diretorioDaClassePrincipalDoApplet = new File(diretorioDosBinarios, "br/ufsc/inf/leb/projetos/dominio");
		diretorioDaClassePrincipalDoApplet.mkdirs();
		String nomeDaClassePrincipalDoApplet = Principal.class.getSimpleName() + ".class";
		InputStream classeDeInicializacaoDoApplet = Principal.class.getResourceAsStream(nomeDaClassePrincipalDoApplet);
		File arquivoDaClasseDeInicializacaoDoApplet = new File(diretorioDaClassePrincipalDoApplet, nomeDaClassePrincipalDoApplet);
		manipuladorDeArquivos.remover(arquivoDaClasseDeInicializacaoDoApplet);
		manipuladorDeArquivos.escreverArquivo(arquivoDaClasseDeInicializacaoDoApplet, classeDeInicializacaoDoApplet);
	}

	private File criarDiretorioDosBinarios(ConfiguracoesProjetos configuracoes, File diretorioDoProjeto, ManipuladorDeArquivos manipuladorDeArquivos) {
		File diretorioDosBinarios = configuracoes.obterDiretorioDosBinarios(diretorioDoProjeto);
		manipuladorDeArquivos.remover(diretorioDosBinarios);
		diretorioDosBinarios.mkdir();
		return diretorioDosBinarios;
	}

}
