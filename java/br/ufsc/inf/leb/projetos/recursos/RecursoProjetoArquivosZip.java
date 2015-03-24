package br.ufsc.inf.leb.projetos.recursos;

import java.io.File;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import br.ufsc.inf.leb.projetos.AmbienteProjetos;
import br.ufsc.inf.leb.projetos.ConfiguracoesProjetos;

@Path("/projeto/{identificador}/arquivos.zip")
public class RecursoProjetoArquivosZip {

	@GET
	@Produces("application/zip")
	public Response obterZip(@PathParam("identificador") String nomeDoProjeto) {
		AmbienteProjetos ambienteProjetos = new AmbienteProjetos();
		ConfiguracoesProjetos configuracoes = ambienteProjetos.obterConfiguracoes();
		File arquivoCompactadoDoProjeto = configuracoes.obterArquivoCompactadoDoProjeto(nomeDoProjeto);
		if (!arquivoCompactadoDoProjeto.exists()) {
			return Response.status(404).build();
		}
		return Response.status(200).entity(arquivoCompactadoDoProjeto).build();
	}

}
