package br.ufsc.inf.leb.agil.dominio;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ExecutorDeComandos {

	private ManipuladorDeArquivos manipuladorDeArquivos;

	public ExecutorDeComandos() {
		manipuladorDeArquivos = new ManipuladorDeArquivos();
	}

	public Boolean executarComandoDeCompilacao(File diretorioDosBinarios, File diretorioDasBibliotecas, File diretorioDosFontes, StringBuilder nomesDosArquivosFontes) throws IOException, InterruptedException {
		String binarios = diretorioDosBinarios.getAbsolutePath();
		String bibliotecas = diretorioDasBibliotecas.getAbsolutePath();
		String fontes = diretorioDosFontes.getAbsolutePath();
		String arquivos = nomesDosArquivosFontes.toString();
		String comando = String.format("javac -d %s -classpath %s/* -sourcepath %s %s", binarios, bibliotecas, fontes, arquivos);
		return executarComando(comando);
	}

	public Boolean executarComandoDeCompactacaoJar(File diretorioDosBinarios, File arquivoJar) throws IOException, InterruptedException {
		String binarios = diretorioDosBinarios.getAbsolutePath();
		String arquivo = arquivoJar.toString();
		String comando = String.format("jar cf %s -C %s .", arquivo, binarios);
		return executarComando(comando);
	}

	private Boolean executarComando(String comando) throws IOException, InterruptedException {
		Process processo = Runtime.getRuntime().exec(comando);
		processo.waitFor(10, TimeUnit.SECONDS);
		manipuladorDeArquivos.escreverNaSaidaPadrao(processo.getInputStream());
		manipuladorDeArquivos.escreverNaSaidaPadrao(processo.getErrorStream());
		return !processo.isAlive() && processo.exitValue() == 0;
	}

}
