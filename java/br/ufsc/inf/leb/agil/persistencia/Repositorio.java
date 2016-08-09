package br.ufsc.inf.leb.projetos.persistencia;

import org.ektorp.CouchDbConnector;
import org.ektorp.support.CouchDbRepositorySupport;

import br.ufsc.inf.leb.projetos.entidades.Documento;

public class Repositorio<T extends Documento> extends CouchDbRepositorySupport<T> {

	protected Repositorio(Class<T> tipo, CouchDbConnector couchDb) {
		super(tipo, couchDb);
		initStandardDesignDocument();
	}

}
