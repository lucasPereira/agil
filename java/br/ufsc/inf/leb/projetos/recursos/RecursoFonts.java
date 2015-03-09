package br.ufsc.inf.leb.projetos.recursos;

import java.io.File;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import br.ufsc.inf.leb.projetos.infraestrutura.CarregadorDeArquivos;

@Path("/fonts/{identificador}")
public class RecursoFonts {

	@GET
	public Response obter(@PathParam("identificador") String identificador) {
		File arquivo = new CarregadorDeArquivos().carregar("fonts", identificador);
		if (arquivo.exists()) {
			return Response.status(200).entity(arquivo).build();
		} else {
			return Response.status(404).build();
		}
	}

}
