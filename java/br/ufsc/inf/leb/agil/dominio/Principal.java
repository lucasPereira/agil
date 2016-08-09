package br.ufsc.inf.leb.agil.dominio;

import java.applet.Applet;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Principal extends Applet {

	private static final long serialVersionUID = 6093153891537147999L;

	@Override
	public void init() {
		System.out.println("Iniciando o applet.");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void start() {
		System.out.println(String.format("Iniciando a classe principal: %s", getParameter("classePrincipal")));
		Class classe;
		try {
			classe = Class.forName(getParameter("classePrincipal"));
			Method metodo = classe.getDeclaredMethod("main", String[].class);
			Object[] argumentos = new Object[1];
			argumentos[0] = new String[0];
			metodo.invoke(null, argumentos);
		} catch (ClassNotFoundException excecao) {
			excecao.printStackTrace();
			System.out.println("Não foi possível encontrar a classe principal.");
		} catch (NoSuchMethodException | SecurityException excecao) {
			excecao.printStackTrace();
			System.out.println("Não foi possível encontrar o método main na classe principal.");
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException excecao) {
			excecao.printStackTrace();
			System.out.println("Ocorreu um erro ao tentar executar o método main da classe principal.");
		}
	}

}
