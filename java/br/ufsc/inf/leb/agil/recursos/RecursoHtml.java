package br.ufsc.inf.leb.agil.recursos;

import java.io.File;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import br.ufsc.inf.leb.agil.dominio.ManipuladorDeArquivos;

@Path("/html/{identificador}")
public class RecursoHtml {

	@GET
	@Produces("text/html")
	public Response colocar(@PathParam("identificador") String identificador) {
		String nome = String.format("%s.html", identificador);
		File arquivo = new ManipuladorDeArquivos().carregarArquivo("html", nome);
		if (arquivo.exists()) {
			return Response.status(200).entity(arquivo).build();
		} else {
			return Response.status(404).build();
		}
	}

}
