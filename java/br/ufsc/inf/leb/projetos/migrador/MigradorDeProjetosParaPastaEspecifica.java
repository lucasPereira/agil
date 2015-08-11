package br.ufsc.inf.leb.projetos.migrador;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

import br.ufsc.inf.leb.projetos.AmbienteProjetos;
import br.ufsc.inf.leb.projetos.ConfiguracoesProjetos;
import br.ufsc.inf.leb.projetos.entidades.Projeto;
import br.ufsc.inf.leb.projetos.persistencia.BancoDeDocumentos;
import br.ufsc.inf.leb.projetos.persistencia.RepositorioDeProjetos;

public class MigradorDeProjetosParaPastaEspecifica {

	public static void main(String[] argumentos) throws IOException {
		new MigradorDeProjetosParaPastaEspecifica().migrar();
	}

	private void migrar() throws IOException {
		AmbienteProjetos ambienteProjetos = new AmbienteProjetos();
		ConfiguracoesProjetos configuracao = ambienteProjetos.obterConfiguracoes();
		BancoDeDocumentos bancoDeDocumentos = ambienteProjetos.obterBancoDeDocumentos();
		RepositorioDeProjetos repositorioDeProjetos = bancoDeDocumentos.obterRepositorioDeProjetos();
		List<Projeto> projetos = repositorioDeProjetos.obterPorIdentificador();
		File diretorioRaiz = new File(configuracao.obterDiretorioDosProjetos(), "ano2015/semestre1");
		for (Projeto projeto : projetos) {
			String nomeAntigo = projeto.obterNome();
			String nomeNovo = "ano2015/semestre1/" + nomeAntigo;
			File diretorioAntigo = configuracao.obterDiretorioDoProjeto(nomeAntigo);
			File diretorioNovo = new File(diretorioRaiz, nomeAntigo);
			projeto.fixarNome(nomeNovo);
			bancoDeDocumentos.atualizarDocumento(projeto);
			diretorioNovo.mkdirs();
			System.out.println(String.format("Movendo %s para %s", diretorioAntigo.getPath(), diretorioNovo.getPath()));
			Files.move(diretorioAntigo.toPath(), diretorioNovo.toPath(), StandardCopyOption.REPLACE_EXISTING);
		}
	}

}
