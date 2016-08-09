package br.ufsc.inf.leb.projetos.dominio;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import br.ufsc.inf.leb.projetos.AmbienteProjetos;

public class ManipuladorDeArquivos {

	private static final Integer BUFFER = 1024;

	public File carregarArquivo(String pasta, String nome) {
		File pastaRaiz = new AmbienteProjetos().obterConfiguracoes().obterPastaRaiz();
		File pastaAtual = new File(pastaRaiz, pasta);
		File arquivo = new File(pastaAtual, nome);
		return arquivo;
	}
	
	public File criarArquivo(File diretorioRaiz, String caminho) throws IOException {
		File arquivo = new File(diretorioRaiz, caminho);
		arquivo.mkdirs();
		arquivo.delete();
		arquivo.createNewFile();
		return arquivo;
	}

	public File criarDiretorio(File diretorioRaiz, String caminho) throws IOException {
		File diretorio = new File(diretorioRaiz, caminho);
		diretorio.mkdirs();
		return diretorio;
	}

	public void criarDiretorio(File diretorio) throws IOException {
		diretorio.mkdirs();
	}

	public File escreverArquivo(InputStream fluxoDeDados, File arquivo) throws IOException {
		Files.copy(fluxoDeDados, arquivo.toPath());
		return arquivo;
	}

	public void escreverNaSaidaPadrao(InputStream fluxoDeDados) throws IOException {
		byte[] buffer = new byte[BUFFER];
		Integer lidos = 0;
		while ((lidos = fluxoDeDados.read(buffer, 0, BUFFER)) > 0) {
			System.out.write(buffer, 0, lidos);
		}
	}

	public void remover(File arquivo) {
		if (arquivo.isDirectory()) {
			for (File arquivoFilho : arquivo.listFiles()) {
				remover(arquivoFilho);
			}
		}
		arquivo.delete();
	}

}
