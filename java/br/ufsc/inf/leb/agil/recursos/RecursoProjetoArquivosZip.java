package br.ufsc.inf.leb.agil.recursos;

import java.io.File;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import br.ufsc.inf.leb.agil.AmbienteAgil;
import br.ufsc.inf.leb.agil.ConfiguracoesAgil;
import br.ufsc.inf.leb.agil.dominio.NomeadorDoProjetoNoSistemaDeArquivos;

@Path("/projeto/{identificador: .+}/arquivos.zip")
public class RecursoProjetoArquivosZip {

	@GET
	@Produces("application/zip")
	public Response obterZip(@PathParam("identificador") String identificador) {
		AmbienteAgil ambiente = new AmbienteAgil();
		ConfiguracoesAgil configuracoes = ambiente.obterConfiguracoes();
		String nomeDoProjetoNoSistemaDeArquivos = new NomeadorDoProjetoNoSistemaDeArquivos().gerar(identificador);
		File arquivoCompactadoDoProjeto = configuracoes.obterArquivoCompactadoDoProjeto(nomeDoProjetoNoSistemaDeArquivos );
		if (!arquivoCompactadoDoProjeto.exists()) {
			return Response.status(404).build();
		}
		return Response.status(200).entity(arquivoCompactadoDoProjeto).build();
	}

}
