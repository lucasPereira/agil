package br.ufsc.inf.leb.agil.restricoesDeInicializacao;

import java.util.LinkedList;
import java.util.List;

public class AsseguradorDeRestricoesDeInicializacao {

	private List<RestricaoDeInicializacao> restricoes;

	public AsseguradorDeRestricoesDeInicializacao() {
		restricoes = new LinkedList<>();
	}

	public void adicionarRestricao(RestricaoDeInicializacao restricao) {
		restricoes.add(restricao);
	}

	public void garantir() {
		List<String> restricoesVioladas = new LinkedList<>();
		for (RestricaoDeInicializacao restricao : restricoes) {
			if (!restricao.garantir()) {
				restricoesVioladas.add(restricao.obterDescricao());
			}
		}
		encerrarOuContinuar(restricoesVioladas);
	}

	private void encerrarOuContinuar(List<String> restricoesVioladas) {
		if (!restricoesVioladas.isEmpty()) {
			System.out.println("Restrições de inicialização violadas:");
			for (String restricaoViolada : restricoesVioladas) {
				System.out.println(restricaoViolada);
			}
			System.out.println("Encerrando incializacao");
			System.exit(restricoesVioladas.size());
		}
	}

}
