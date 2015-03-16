package br.ufsc.inf.leb.projetos.recursos;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import br.ufsc.inf.leb.projetos.AmbienteProjetos;
import br.ufsc.inf.leb.projetos.ConfiguracoesProjetos;
import br.ufsc.inf.leb.projetos.dominio.Principal;
import br.ufsc.inf.leb.projetos.entidades.Projeto;
import br.ufsc.inf.leb.projetos.infraestrutura.ManipuladorDeArquivos;
import br.ufsc.inf.leb.projetos.persistencia.BancoDeDocumentos;
import br.ufsc.inf.leb.projetos.persistencia.RepositorioDeProjetos;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@Path("/projeto/{identificador}/classePrincipal")
public class RecursoProjetoClassePrincipal {

	@PUT
	@Consumes("application/json")
	public Response obter(@PathParam("identificador") String identificador, JsonObject dados) throws IOException {
		AmbienteProjetos ambienteProjetos = new AmbienteProjetos();
		BancoDeDocumentos bancoDeDocumentos = ambienteProjetos.obterBancoDeDocumentos();
		RepositorioDeProjetos repositorioDeProjetos = bancoDeDocumentos.obterRepositorioDeProjetos();
		List<Projeto> projetos = repositorioDeProjetos.obterPorNome(identificador);
		if (projetos.size() > 1) {
			return Response.status(409).build();
		}
		if (projetos.size() < 1) {
			return Response.status(404).build();
		}
		if (dados.isJsonObject() && dados.has("caminho")) {
			JsonElement caminhoJson = dados.get("caminho");
			if (caminhoJson.isJsonPrimitive()) {
				String caminho = caminhoJson.getAsString();
				ConfiguracoesProjetos configuracoes = ambienteProjetos.obterConfiguracoes();
				File diretorioDosProjetos = configuracoes.obterDiretorioDosArquivosDosProjetos();
				File diretorioDoProjeto = new File(diretorioDosProjetos, identificador);
				File classePrincipal = new File(diretorioDoProjeto, caminho);
				if (!classePrincipal.exists()) {
					return Response.status(404).build();
				}
				File diretorioDosBinarios = configuracoes.obterDiretorioDosBinarios(diretorioDoProjeto);
				ManipuladorDeArquivos manipuladorDeArquivos = new ManipuladorDeArquivos();
				manipuladorDeArquivos.remover(diretorioDosBinarios);
				diretorioDosBinarios.mkdir();
				InputStream classeDeInicializacaoDoApplet = Principal.class.getResourceAsStream("");
				File arquivoDaClasseDeInicializacaoDoApplet = new File(diretorioDosBinarios, Principal.class.getName() + ".class");
				manipuladorDeArquivos.escreverArquivo(arquivoDaClasseDeInicializacaoDoApplet, classeDeInicializacaoDoApplet);
				// TODO gerar classe principal
				// TODO compilar
				Projeto projeto = projetos.get(0);
				String nomeDaClassePrincipal = caminho.replaceFirst(identificador + "/" + configuracoes.obterNomeDiretorioDosFontes() + "/", "");
				nomeDaClassePrincipal = caminho.replaceAll("/", ".");
				projeto.fixarClassePrincipal(nomeDaClassePrincipal);
				bancoDeDocumentos.atualizarDocumento(projeto);
				return Response.status(200).build();
			} else {
				return Response.status(400).build();
			}
		} else {
			return Response.status(404).build();
		}
	}

}
