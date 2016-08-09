package br.ufsc.inf.leb.agil.persistencia;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;

import br.ufsc.inf.leb.agil.ConfiguracoesAgil;
import br.ufsc.inf.leb.agil.entidades.Documento;
import br.ufsc.inf.leb.agil.persistencia.serializacao.FabricaDeMapeadoresDoCouchDB;

public class BancoDeDocumentos {

	private CouchDbInstance instanciaDoCouch;
	private CouchDbConnector conexaoDoCouch;

	public BancoDeDocumentos(ConfiguracoesAgil configuracoes) {
		HttpClient clienteHttp = new StdHttpClient.Builder().build();
		instanciaDoCouch = new StdCouchDbInstance(clienteHttp, new FabricaDeMapeadoresDoCouchDB());
		conexaoDoCouch = instanciaDoCouch.createConnector(configuracoes.obterNomeDaBaseDeDocumentos(), true);
		System.setProperty("org.ektorp.support.AutoUpdateViewOnChange", "true");
	}

	public void inserirDocumento(Documento documento) {
		conexaoDoCouch.create(documento);
	}

	public void atualizarDocumento(Documento documento) {
		conexaoDoCouch.update(documento);
	}

	public void removerDocumento(Documento documento) {
		conexaoDoCouch.delete(documento);
	}

	public RepositorioDeProjetos obterRepositorioDeProjetos() {
		return new RepositorioDeProjetos(conexaoDoCouch);
	}

}
