package br.ufsc.inf.leb.projetos.recursos;

import java.io.File;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import br.ufsc.inf.leb.projetos.AmbienteProjetos;
import br.ufsc.inf.leb.projetos.ConfiguracoesProjetos;
import br.ufsc.inf.leb.projetos.dominio.NomeadorDoProjetoNoSistemaDeArquivos;

@Path("/projeto/{identificador: .+}/arquivo/{caminho: .+}")
public class RecursoProjetoArquivo {

	@GET
	@Produces("application/octet-stream")
	public Response obter(@PathParam("identificador") String identificador, @PathParam("caminho") String caminho) {
		ConfiguracoesProjetos configuracoesProjeto = new AmbienteProjetos().obterConfiguracoes();
		String nomeDoProjetoNoSistemaDeArquivos = new NomeadorDoProjetoNoSistemaDeArquivos().gerar(identificador);
		File arquivoSolicitado = configuracoesProjeto.obterArquivoDoProjeto(nomeDoProjetoNoSistemaDeArquivos, caminho);
		if (!arquivoSolicitado.exists()) {
			return Response.status(404).build();
		}
		return Response.status(200).entity(arquivoSolicitado).build();
	}

}
