package br.ufsc.inf.leb.projetos.dominio;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import br.ufsc.inf.leb.projetos.AmbienteProjetos;
import br.ufsc.inf.leb.projetos.infraestrutura.ManipuladorDeArquivos;

public class ArquivosPermitidos {

	private static Long CONTADOR = 0L;

	Boolean recursivo;
	List<Pattern> arquivosPermitidos;
	Map<String, ArquivosPermitidos> diretoriosPorNome;

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

	public void salvarArquivos(String nomeDoProjeto, InputStream arquivoCompactado) throws IOException, ExcecaoDeArquivoCompactadoNoFormatoInvalido, ZipException {
		ManipuladorDeArquivos manipuladorDeArquivos = new ManipuladorDeArquivos();

		File diretorioTemporarioDosProjetos = new AmbienteProjetos().obterConfiguracoes().obterDiretorioDosArquivosTemporariosDosProjetos();
		File diretorioTemporarioDoProjeto = new File(diretorioTemporarioDosProjetos, nomeDoProjeto + obterIdentificadorAtual());
		File diretorioDosProjetos = new AmbienteProjetos().obterConfiguracoes().obterDiretorioDosArquivosDosProjetos();
		File diretorioDoProjeto = new File(diretorioDosProjetos, nomeDoProjeto);

		File arquivoTemporarioCompactadoDoProjeto = new File(diretorioTemporarioDoProjeto + ".zip");
		Files.copy(arquivoCompactado, arquivoTemporarioCompactadoDoProjeto.toPath());
		ZipFile arquivoZipCompactadoDoProjeto = new ZipFile(arquivoTemporarioCompactadoDoProjeto);
		arquivoZipCompactadoDoProjeto.extractAll(diretorioTemporarioDoProjeto.getAbsolutePath());
		diretorioTemporarioDoProjeto = entrarNoPrimeiroDiretorio(diretorioTemporarioDoProjeto);

		removerNaoPermitidos(manipuladorDeArquivos, diretorioTemporarioDoProjeto);
		manipuladorDeArquivos.remover(diretorioDoProjeto);
		diretorioTemporarioDoProjeto.renameTo(diretorioDoProjeto);
		manipuladorDeArquivos.remover(diretorioTemporarioDoProjeto);
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

	private static synchronized Long obterIdentificadorAtual() {
		return CONTADOR++;
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
