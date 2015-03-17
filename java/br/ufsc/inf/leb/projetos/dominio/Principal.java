package br.ufsc.inf.leb.projetos.dominio;

import java.applet.Applet;
import java.lang.reflect.Method;

public class Principal extends Applet {

	private static final long serialVersionUID = 6093153891537147999L;

	public static void main(String[] args) {
		System.out.println("Só pra compilar");
	}

	@Override
	public void init() {
		System.out.println("Iniciando o applet.");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void start() {
		System.out.println("Recebendo classePrincipal");
		System.out.println(getParameter("classePrincipal"));
		try {
			Class classe = Class.forName(getParameter("classePrincipal"));
			Method metodo = classe.getDeclaredMethod("main", String[].class);
			Object[] argumentos = new Object[1];
			argumentos[0] = new String[0];
			metodo.invoke(null, argumentos);
		} catch (Exception excecao) {
			excecao.printStackTrace();
			System.out.println("Não foi possível iniciar a classe principal. Certifique-se de que o método main foi definido.");
		}
	}

}
