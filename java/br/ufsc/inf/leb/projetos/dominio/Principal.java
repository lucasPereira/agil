package br.ufsc.inf.leb.projetos.dominio;

import java.applet.Applet;

import javax.swing.JOptionPane;

public class Principal extends Applet {

	private static final long serialVersionUID = 6093153891537147999L;

	@Override
	public void init() {
		System.out.println("INICIO");
	}

	@Override
	public void start() {
		JOptionPane.showInputDialog("Olá, tudo bem?");
		try {
			Thread.sleep(10000);
		} catch (InterruptedException excecao) {
			excecao.printStackTrace();
		}
	}

}
