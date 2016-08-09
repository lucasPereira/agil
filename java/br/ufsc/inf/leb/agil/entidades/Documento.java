package br.ufsc.inf.leb.agil.entidades;

import org.ektorp.support.CouchDbDocument;

public class Documento extends CouchDbDocument {

	private static final long serialVersionUID = -8245065977647808084L;

	public String obterIdentificador() {
		return getId();
	}

}
