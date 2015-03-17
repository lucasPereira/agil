package br.ufsc.inf.leb.projetos.dominio;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import br.ufsc.inf.leb.projetos.AmbienteProjetos;
import br.ufsc.inf.leb.projetos.ConfiguracoesProjetos;
import br.ufsc.inf.leb.projetos.entidades.Nodo;

public class ArvoreDoProjeto {

	private static final Integer TAMANHO_MAXIMO_PARA_LETURA = 1024 * 1024;
	private static final List<String> EXTENSOES_TEXTUAIS = Arrays.asList(".classpath", ".project", ".java", ".txt");

	public Nodo construirArvore(String nomeDoProjeto) throws IOException {
		File diretorioDosProjetos = new AmbienteProjetos().obterConfiguracoes().obterDiretorioDosArquivosDosProjetos();
		File diretorioDoProjeto = new File(diretorioDosProjetos, nomeDoProjeto);
		return construirArvore(diretorioDoProjeto);
	}

	private Nodo construirArvore(File arquivo) throws IOException {
		if (arquivo.exists()) {
			String nome = arquivo.getName();
			Nodo nodo = (arquivo.isDirectory()) ? Nodo.construirDiretorio(nome) : Nodo.construirArquivo(nome);
			adicionarConteudoTextualSeNecessario(nodo, arquivo);
			adicionarFilhosSeNecessario(nodo, arquivo);
			return nodo;
		}
		return null;
	}

	private Nodo construirArvore(Nodo pai, File arquivo) throws IOException {
		String nome = arquivo.getName();
		Nodo nodo = (arquivo.isDirectory()) ? Nodo.construirDiretorio(pai, nome) : Nodo.construirArquivo(pai, nome);
		adicionarConteudoTextualSeNecessario(nodo, arquivo);
		adicionarFilhosSeNecessario(nodo, arquivo);
		return nodo;
	}

	private void adicionarFilhosSeNecessario(Nodo nodo, File arquivo) throws IOException {
		ConfiguracoesProjetos configuracoes = new AmbienteProjetos().obterConfiguracoes();
		String nomeDoArquivo = arquivo.getName();
		Boolean diretorioDosBinarios = configuracoes.obterNomeDiretorioDosBinarios().equals(nomeDoArquivo);
		Boolean diretorioDosZips = configuracoes.obterNomeDiretorioDosZips().equals(nomeDoArquivo);
		if (arquivo.isDirectory() && !diretorioDosBinarios && !diretorioDosZips) {
			for (File filho : arquivo.listFiles()) {
				nomeDoArquivo = filho.getName();
				diretorioDosBinarios = configuracoes.obterNomeDiretorioDosBinarios().equals(nomeDoArquivo);
				diretorioDosZips = configuracoes.obterNomeDiretorioDosZips().equals(nomeDoArquivo);
				if (!diretorioDosBinarios && !diretorioDosZips) {
					nodo.adicionarFilho(construirArvore(nodo, filho));
				}
			}
		}
	}

	private void adicionarConteudoTextualSeNecessario(Nodo nodo, File arquivo) throws IOException {
		if (arquivo.isFile() && naoExcedeOTamanhoMaximoDeLeitura(arquivo) && possuiExtensaoTextual(arquivo)) {
			StringBuilder construtorDoConteudo = new StringBuilder();
			for (String linha : Files.readAllLines(arquivo.toPath())) {
				construtorDoConteudo.append(linha);
				construtorDoConteudo.append("\n");
			}
			nodo.fixarConteudo(construtorDoConteudo.toString());
		}
	}

	private Boolean possuiExtensaoTextual(File arquivo) {
		for (String extensaoPermitida : EXTENSOES_TEXTUAIS) {
			if (arquivo.getName().endsWith(extensaoPermitida)) {
				return true;
			}
		}
		return false;
	}

	private Boolean naoExcedeOTamanhoMaximoDeLeitura(File arquivo) {
		return arquivo.length() < TAMANHO_MAXIMO_PARA_LETURA;
	}

}
