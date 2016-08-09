package br.ufsc.inf.leb.projetos.recursos;

import java.io.File;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import br.ufsc.inf.leb.projetos.dominio.ManipuladorDeArquivos;

@Path("/css/{identificador}")
public class RecursoCss {

	@GET
	@Produces("text/css")
	public Response obter(@PathParam("identificador") String identificador) {
		String nome = String.format("%s.css", identificador);
		File arquivo = new ManipuladorDeArquivos().carregarArquivo("css", nome);
		if (arquivo.exists()) {
			return Response.status(200).entity(arquivo).build();
		} else {
			return Response.status(404).build();
		}
	}

}
