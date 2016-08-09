package br.ufsc.inf.leb.agil.recursos;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import br.ufsc.inf.leb.agil.AmbienteAgil;
import br.ufsc.inf.leb.agil.entidades.Projeto;
import br.ufsc.inf.leb.agil.persistencia.BancoDeDocumentos;
import br.ufsc.inf.leb.agil.persistencia.RepositorioDeProjetos;

@Path("/projetos")
public class RecursoProjetos {

	@GET
	@Produces("application/json")
	public Response obter() {
		AmbienteAgil ambiente = new AmbienteAgil();
		BancoDeDocumentos bancoDeDocumentos = ambiente.obterBancoDeDocumentos();
		RepositorioDeProjetos repositorioDeProjetos = bancoDeDocumentos.obterRepositorioDeProjetos();
		List<Projeto> projetos = repositorioDeProjetos.obterPorIdentificador();
		return Response.status(200).entity(projetos).build();
	}

}
