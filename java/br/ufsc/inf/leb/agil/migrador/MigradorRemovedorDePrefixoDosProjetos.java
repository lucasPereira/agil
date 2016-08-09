package br.ufsc.inf.leb.agil.migrador;

import java.io.IOException;
import java.util.List;

import br.ufsc.inf.leb.agil.AmbienteAgil;
import br.ufsc.inf.leb.agil.entidades.Projeto;
import br.ufsc.inf.leb.agil.persistencia.BancoDeDocumentos;
import br.ufsc.inf.leb.agil.persistencia.RepositorioDeProjetos;

public class MigradorRemovedorDePrefixoDosProjetos {

	public static void main(String[] argumentos) throws IOException {
		new MigradorRemovedorDePrefixoDosProjetos().migrar();
	}

	private void migrar() throws IOException {
		AmbienteAgil ambiente = new AmbienteAgil();
		BancoDeDocumentos bancoDeDocumentos = ambiente.obterBancoDeDocumentos();
		RepositorioDeProjetos repositorioDeProjetos = bancoDeDocumentos.obterRepositorioDeProjetos();
		List<Projeto> projetos = repositorioDeProjetos.obterPorIdentificador();
		for (Projeto projeto : projetos) {
			projeto.fixarNome(projeto.obterNome().replaceAll("ano2015/semestre1/", ""));
			bancoDeDocumentos.atualizarDocumento(projeto);
		}
	}

}
