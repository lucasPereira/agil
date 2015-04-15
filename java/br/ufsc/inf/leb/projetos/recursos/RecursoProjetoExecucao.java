package br.ufsc.inf.leb.projetos.recursos;

import java.io.File;
import java.net.URI;
import java.util.Date;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import br.ufsc.inf.leb.projetos.AmbienteProjetos;
import br.ufsc.inf.leb.projetos.ConfiguracoesProjetos;
import br.ufsc.inf.leb.projetos.dominio.NomeadorDoProjetoNoSistemaDeArquivos;
import br.ufsc.inf.leb.projetos.entidades.Projeto;
import br.ufsc.inf.leb.projetos.persistencia.BancoDeDocumentos;
import br.ufsc.inf.leb.projetos.persistencia.RepositorioDeProjetos;

@Path("/projeto/{identificador: .+}/execucao")
public class RecursoProjetoExecucao {

	@GET
	@Path("{arquivo: .+}")
	public Response obterArquivo(@PathParam("identificador") String identificador, @PathParam("arquivo") String arquivo) {
		ConfiguracoesProjetos configuracoesProjeto = new AmbienteProjetos().obterConfiguracoes();
		String nomeDoProjetoNoSistemaDeArquivos = new NomeadorDoProjetoNoSistemaDeArquivos().gerar(identificador);
		File arquivoSolicitado = configuracoesProjeto.obterArquivoDeExecucaoDoProjeto(nomeDoProjetoNoSistemaDeArquivos, arquivo);
		if (!arquivoSolicitado.exists()) {
			return Response.status(404).build();
		}
		return Response.status(200).entity(arquivoSolicitado).build();
	}

	@GET
	@Produces("text/html")
	public Response obter(@PathParam("identificador") String identificador) {
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
		URI uriDeExecucao = ambienteProjetos.obterConfiguracoes().coonstruirUri(RecursoProjetoExecucao.class, identificador);
		StringBuilder html = new StringBuilder();
		html.append("<!DOCTYPE html>");
		html.append("\n");
		html.append("<html>");
		html.append("\n");
		html.append("<head>");
		html.append("\n");
		html.append("	<meta charset=\"utf-8\" />");
		html.append("\n");
		html.append(String.format("	<title>%s</title>", identificador));
		html.append("\n");
		html.append("</head>");
		html.append("\n");
		html.append("<body>");
		html.append("\n");
		html.append("<applet");
		html.append("\n");
		html.append(String.format("codebase=\"%s\"", uriDeExecucao.toASCIIString()));
		html.append("\n");
		ConfiguracoesProjetos configuracoes = ambienteProjetos.obterConfiguracoes();
		html.append(String.format("archive=\"%s?versao=%d\"", configuracoes.obterArquivoJarDoProjeto(identificador).getName(), new Date().getTime()));
		html.append("\n");
		html.append("code=\"br.ufsc.inf.leb.projetos.dominio.Principal.class\"");
		html.append("\n");
		html.append(String.format("><param name=\"classePrincipal\" value=\"%s\" /></applet>", projetos.get(0).obterClassePrincipal()));
		html.append("\n");
		html.append("</body>");
		html.append("\n");
		html.append("</html>");
		return Response.status(200).entity(html.toString()).build();
	}

}
