package br.ufsc.inf.leb.projetos.infraestrutura;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ManipuladorDeArquivos {

	private static final Integer BUFFER = 1024;

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

	public void criarDiretorio(File diretorioRaiz) throws IOException {
		diretorioRaiz.mkdirs();
	}

	public File escreverArquivo(File arquivo, InputStream fluxoDeDados) throws IOException {
		FileOutputStream saida = new FileOutputStream(arquivo);
		byte[] buffer = new byte[BUFFER];
		Integer lidos = 0;
		while ((lidos = fluxoDeDados.read(buffer, 0, BUFFER)) > 0) {
			saida.write(buffer, 0, lidos);
		}
		saida.flush();
		saida.close();
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
