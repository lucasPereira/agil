package br.ufsc.inf.leb.projetos.recursos;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import br.ufsc.inf.leb.projetos.AmbienteProjetos;
import br.ufsc.inf.leb.projetos.entidades.Projeto;
import br.ufsc.inf.leb.projetos.persistencia.BancoDeDocumentos;
import br.ufsc.inf.leb.projetos.persistencia.RepositorioDeProjetos;

@Path("/projetos")
public class RecursoDiretorios {

	@GET
	@Produces("application/json")
	public Response obter() {
		AmbienteProjetos ambienteProjetos = new AmbienteProjetos();
		BancoDeDocumentos bancoDeDocumentos = ambienteProjetos.obterBancoDeDocumentos();
		RepositorioDeProjetos repositorioDeProjetos = bancoDeDocumentos.obterRepositorioDeProjetos();
		List<Projeto> projetos = repositorioDeProjetos.obterPorIdentificador();
		return Response.status(200).entity(projetos).build();
	}

}
