package br.ufsc.inf.leb.projetos.dominio;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import br.ufsc.inf.leb.projetos.AmbienteProjetos;
import br.ufsc.inf.leb.projetos.ConfiguracoesProjetos;

public class ArquivosPermitidos {

	private Boolean recursivo;
	private List<Pattern> arquivosPermitidos;
	private Map<String, ArquivosPermitidos> diretoriosPorNome;

	public ArquivosPermitidos() {
		arquivosPermitidos = new LinkedList<>();
		recursivo = false;
		diretoriosPorNome = new HashMap<>();
	}

	public ArquivosPermitidos permitirArquivo(String nome) {
		Pattern padrao = Pattern.compile(Pattern.quote(nome));
		arquivosPermitidos.add(padrao);
		return this;
	}

	public ArquivosPermitidos permitirArquivosComExtensao(String extensao) {
		Pattern padrao = Pattern.compile(".+" + Pattern.quote("." + extensao));
		arquivosPermitidos.add(padrao);
		return this;
	}

	public ArquivosPermitidos permitirTodosArquivos() {
		Pattern padrao = Pattern.compile(".+");
		arquivosPermitidos.add(padrao);
		return this;
	}

	public ArquivosPermitidos permitirDiretorio(String nome, ArquivosPermitidos arquivosPermitidosDoDiretorio) {
		diretoriosPorNome.put(nome, arquivosPermitidosDoDiretorio);
		return this;
	}

	public ArquivosPermitidos recursivo() {
		recursivo = true;
		return this;
	}

	public void salvarArquivos(String nomeDoProjeto, InputStream fluxoDeDados) throws IOException, ExcecaoDeArquivoCompactadoNoFormatoInvalido, ZipException {
		ManipuladorDeArquivos manipuladorDeArquivos = new ManipuladorDeArquivos();
		GeradorDeNomesUnicos geradorDeNomesUnicos = new GeradorDeNomesUnicos();
		AmbienteProjetos ambienteProjetos = new AmbienteProjetos();
		ConfiguracoesProjetos configuracoesProjetos = ambienteProjetos.obterConfiguracoes();
		String nomeTemporarioDoProjeto = geradorDeNomesUnicos.gerar();
		File diretorioDosArquivosDoProjeto = configuracoesProjetos.obterDiretorioDosArquivosDoProjeto(nomeDoProjeto);
		File diretorioDosArquivosDoProjetoTemporario = configuracoesProjetos.obterDiretorioDosArquivosDoProjetoTemporario(nomeTemporarioDoProjeto);
		File arquivoCompactadoDoProjeto = configuracoesProjetos.obterArquivoCompactadoDoProjeto(nomeDoProjeto);
		File arquivoCompactadoDoProjetoTemporario = configuracoesProjetos.obterArquivoCompactadoDoProjetoTemporario(nomeTemporarioDoProjeto);
		manipuladorDeArquivos.escreverArquivo(fluxoDeDados, arquivoCompactadoDoProjetoTemporario);
		extrairArquivoCompactadoDoProjeto(diretorioDosArquivosDoProjetoTemporario, arquivoCompactadoDoProjetoTemporario);
		File diretorioTemporarioDoProjetoRaiz = entrarNoPrimeiroDiretorio(diretorioDosArquivosDoProjetoTemporario);
		removerNaoPermitidos(manipuladorDeArquivos, diretorioTemporarioDoProjetoRaiz);
		manipuladorDeArquivos.remover(diretorioDosArquivosDoProjeto);
		manipuladorDeArquivos.remover(arquivoCompactadoDoProjeto);
		manipuladorDeArquivos.criarDiretorio(diretorioDosArquivosDoProjeto);
		diretorioTemporarioDoProjetoRaiz.renameTo(diretorioDosArquivosDoProjeto);
		manipuladorDeArquivos.remover(diretorioDosArquivosDoProjetoTemporario);
		manipuladorDeArquivos.remover(arquivoCompactadoDoProjetoTemporario);
		manipuladorDeArquivos.remover(arquivoCompactadoDoProjeto);
		compactarArquivosDoProjeto(nomeDoProjeto, diretorioDosArquivosDoProjeto, arquivoCompactadoDoProjeto);
	}

	private void compactarArquivosDoProjeto(String nomeDoProjeto, File diretorioDosArquivosDoProjeto, File arquivoCompactadoDoProjeto) throws ZipException {
		ZipFile arquivoZipCompactadoDoProjeto = new ZipFile(arquivoCompactadoDoProjeto);
		ZipParameters parametros = new ZipParameters();
		arquivoZipCompactadoDoProjeto.createZipFileFromFolder(diretorioDosArquivosDoProjeto, parametros, false, 0);
	}

	private void extrairArquivoCompactadoDoProjeto(File diretorioTemporarioDoProjeto, File arquivoTemporarioCompactadoDoProjeto) throws ZipException {
		ZipFile arquivoZipCompactadoDoProjetoTemporario = new ZipFile(arquivoTemporarioCompactadoDoProjeto);
		arquivoZipCompactadoDoProjetoTemporario.extractAll(diretorioTemporarioDoProjeto.getAbsolutePath());
	}

	private File entrarNoPrimeiroDiretorio(File diretorioTemporarioDoProjeto) throws ExcecaoDeArquivoCompactadoNoFormatoInvalido {
		if (diretorioTemporarioDoProjeto.listFiles().length != 1 || !diretorioTemporarioDoProjeto.listFiles()[0].isDirectory()) {
			throw new ExcecaoDeArquivoCompactadoNoFormatoInvalido();
		}
		return diretorioTemporarioDoProjeto.listFiles()[0];
	}

	private void removerNaoPermitidos(ManipuladorDeArquivos manipuladorDeArquivos, File diretorio) {
		for (File arquivo : diretorio.listFiles()) {
			if (arquivo.isFile()) {
				removerArquivoSeNecessario(manipuladorDeArquivos, arquivo);
			}
			if (arquivo.isDirectory()) {
				removerDiretorioSeNecessario(manipuladorDeArquivos, arquivo);
			}
		}
	}

	private void removerDiretorioSeNecessario(ManipuladorDeArquivos manipuladorDeArquivos, File diretorio) {
		if (recursivo) {
			removerNaoPermitidos(manipuladorDeArquivos, diretorio);
		} else {
			String nomeDoDiretorio = diretorio.getName();
			Boolean incluirDiretorio = diretorioDeveSerIncluido(nomeDoDiretorio);
			if (!incluirDiretorio) {
				manipuladorDeArquivos.remover(diretorio);
			} else {
				diretoriosPorNome.get(nomeDoDiretorio).removerNaoPermitidos(manipuladorDeArquivos, diretorio);
			}
		}
	}

	private void removerArquivoSeNecessario(ManipuladorDeArquivos manipuladorDeArquivos, File arquivo) {
		Boolean incluirArquivo = arquivoDeveSerIncluido(arquivo.getName());
		if (!incluirArquivo) {
			manipuladorDeArquivos.remover(arquivo);
		}
	}

	private Boolean arquivoDeveSerIncluido(String nome) {
		for (Pattern arquivoPermitido : arquivosPermitidos) {
			if (arquivoPermitido.matcher(nome).matches()) {
				return true;
			}
		}
		return false;
	}

	private Boolean diretorioDeveSerIncluido(String nome) {
		return diretoriosPorNome.containsKey(nome);
	}

}
