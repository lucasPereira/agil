package br.ufsc.inf.leb.projetos.migrador;

import java.io.IOException;
import java.util.List;

import br.ufsc.inf.leb.projetos.AmbienteProjetos;
import br.ufsc.inf.leb.projetos.entidades.Projeto;
import br.ufsc.inf.leb.projetos.persistencia.BancoDeDocumentos;
import br.ufsc.inf.leb.projetos.persistencia.RepositorioDeProjetos;

public class MigradorRemovedorDePrefixoDosProjetos {

	public static void main(String[] argumentos) throws IOException {
		new MigradorRemovedorDePrefixoDosProjetos().migrar();
	}

	private void migrar() throws IOException {
		AmbienteProjetos ambienteProjetos = new AmbienteProjetos();
		BancoDeDocumentos bancoDeDocumentos = ambienteProjetos.obterBancoDeDocumentos();
		RepositorioDeProjetos repositorioDeProjetos = bancoDeDocumentos.obterRepositorioDeProjetos();
		List<Projeto> projetos = repositorioDeProjetos.obterPorIdentificador();
		for (Projeto projeto : projetos) {
			projeto.fixarNome(projeto.obterNome().replaceAll("ano2015/semestre1/", ""));
			bancoDeDocumentos.atualizarDocumento(projeto);
		}
	}

}
