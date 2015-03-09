package br.ufsc.inf.leb.projetos.recursos;

import java.io.File;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import br.ufsc.inf.leb.projetos.infraestrutura.CarregadorDeArquivos;

@Path("/")
public class RecursoRaiz {

	@GET
	@Produces("text/html")
	public Response obter() {
		File arquivo = new CarregadorDeArquivos().carregar("html", "raiz.html");
		return Response.status(200).entity(arquivo).build();
	}

}
