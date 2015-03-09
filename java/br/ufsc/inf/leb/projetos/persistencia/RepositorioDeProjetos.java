package br.ufsc.inf.leb.projetos.persistencia;

import java.util.List;

import org.ektorp.CouchDbConnector;
import org.ektorp.support.View;
import org.ektorp.support.Views;

import br.ufsc.inf.leb.projetos.entidades.Projeto;

@Views({
		@View(name = "porIdentificador", map = "function (documento) { if (documento.tipo === 'projeto') { emit(documento._id, documento); } }"),
		@View(name = "porNome", map = "function (documento) { if (documento.tipo === 'projeto') { emit(documento.nome, documento); } }")
})
public class RepositorioDeProjetos extends Repositorio<Projeto> {

	protected RepositorioDeProjetos(CouchDbConnector couchDb) {
		super(Projeto.class, couchDb);
	}

	public List<Projeto> obterPorIdentificador() {
		return queryView("porIdentificador");
	}

	public List<Projeto> obterPorNome(String nome) {
		return queryView("porNome", nome);
	}

}
