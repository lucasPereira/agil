package br.ufsc.inf.leb.agil.recursos;

import java.net.URI;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import br.ufsc.inf.leb.agil.AmbienteAgil;
import br.ufsc.inf.leb.agil.ConfiguracoesAgil;
import br.ufsc.inf.leb.agil.entidades.Projeto;
import br.ufsc.inf.leb.agil.persistencia.BancoDeDocumentos;
import br.ufsc.inf.leb.agil.persistencia.RepositorioDeProjetos;

@Path("/projeto/{identificador: .+}")
public class RecursoProjeto {

	@GET
	@Produces("application/json")
	public Response obter(@PathParam("identificador") String identificador) {
		AmbienteAgil ambiente = new AmbienteAgil();
		BancoDeDocumentos bancoDeDocumentos = ambiente.obterBancoDeDocumentos();
		RepositorioDeProjetos repositorioDeProjetos = bancoDeDocumentos.obterRepositorioDeProjetos();
		List<Projeto> projetos = repositorioDeProjetos.obterPorNome(identificador);
		if (projetos.size() > 1) {
			return Response.status(409).build();
		}
		if (projetos.size() < 1) {
			return Response.status(404).build();
		}
		return Response.status(200).entity(projetos.get(0)).build();
	}

	@PUT
	@Produces("application/json")
	public Response colocar(@PathParam("identificador") String identificador) {
		AmbienteAgil ambiente = new AmbienteAgil();
		ConfiguracoesAgil configuracoes = ambiente.obterConfiguracoes();
		BancoDeDocumentos bancoDeDocumentos = ambiente.obterBancoDeDocumentos();
		RepositorioDeProjetos repositorioDeProjetos = bancoDeDocumentos.obterRepositorioDeProjetos();
		if (!identificador.matches(configuracoes.obterExpressaoRegularParaValidacaoDoNomeDoProjeto())) {
			return Response.status(400).build();
		}
		if (existeDiretorioComNomeDeProjeto(identificador, repositorioDeProjetos)) {
			return Response.status(409).build();
		}
		Projeto projeto = new Projeto(identificador);
		bancoDeDocumentos.inserirDocumento(projeto);
		List<Projeto> projetos = repositorioDeProjetos.obterPorNome(identificador);
		if (projetos.size() > 1) {
			bancoDeDocumentos.removerDocumento(projeto);
			return Response.status(409).build();
		}
		URI uri = configuracoes.coonstruirUri(RecursoProjeto.class, identificador);
		return Response.status(201).location(uri).build();
	}

	private Boolean existeDiretorioComNomeDeProjeto(String identificador, RepositorioDeProjetos repositorioDeProjetos) {
		String[] nomes = identificador.split("/");
		String nomeAtual = "";
		for (String nome : nomes) {
			nomeAtual += nome;
			List<Projeto> projetos = repositorioDeProjetos.obterPorNome(nomeAtual);
			if (!projetos.isEmpty()) {
				return true;
			}
			nomeAtual += "/";
		}
		return false;
	}

}
