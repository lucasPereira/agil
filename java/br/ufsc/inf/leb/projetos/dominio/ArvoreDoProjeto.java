package br.ufsc.inf.leb.projetos.dominio;

import java.io.File;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
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
		ConfiguracoesProjetos configuracoesProjetos = new AmbienteProjetos().obterConfiguracoes();
		File diretorioDosArquivosDoProjeto = configuracoesProjetos.obterDiretorioDosArquivosDoProjeto(nomeDoProjeto);
		return construirArvore(diretorioDosArquivosDoProjeto, nomeDoProjeto);
	}

	private Nodo construirArvore(File arquivo, String nomeDoProjeto) throws IOException {
		if (arquivo.exists()) {
			Nodo nodo = (arquivo.isDirectory()) ? Nodo.construirDiretorio(nomeDoProjeto) : Nodo.construirArquivo(nomeDoProjeto);
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
		if (arquivo.isDirectory()) {
			for (File filho : arquivo.listFiles()) {
				nodo.adicionarFilho(construirArvore(nodo, filho));
			}
		}
	}

	private void adicionarConteudoTextualSeNecessario(Nodo nodo, File arquivo) throws IOException {
		if (arquivo.isFile() && naoExcedeOTamanhoMaximoDeLeitura(arquivo) && possuiExtensaoTextual(arquivo)) {
			StringBuilder construtorDoConteudo = new StringBuilder();
			try {
				for (String linha : Files.readAllLines(arquivo.toPath(), StandardCharsets.UTF_8)) {
					construtorDoConteudo.append(linha);
					construtorDoConteudo.append("\n");
				}
			} catch (MalformedInputException excecao) {
				excecao.printStackTrace();
				for (String linha : Files.readAllLines(arquivo.toPath(), StandardCharsets.ISO_8859_1)) {
					construtorDoConteudo.append(linha);
					construtorDoConteudo.append("\n");
				}
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
