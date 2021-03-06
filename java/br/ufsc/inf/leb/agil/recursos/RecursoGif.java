package br.ufsc.inf.leb.agil.recursos;

import java.io.File;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import br.ufsc.inf.leb.agil.dominio.ManipuladorDeArquivos;

@Path("/gif/{identificador}")
public class RecursoGif {

	@GET
	@Produces("image/gif")
	public Response obter(@PathParam("identificador") String identificador) {
		String nome = String.format("%s.gif", identificador);
		File arquivo = new ManipuladorDeArquivos().carregarArquivo("gif", nome);
		if (!arquivo.exists()) {
			return Response.status(404).build();
		}
		return Response.status(200).entity(arquivo).build();
	}

}
