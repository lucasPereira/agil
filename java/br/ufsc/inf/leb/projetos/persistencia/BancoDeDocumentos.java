package br.ufsc.inf.leb.projetos.persistencia;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;

import br.ufsc.inf.leb.projetos.ConfiguracoesProjetos;
import br.ufsc.inf.leb.projetos.entidades.Documento;
import br.ufsc.inf.leb.projetos.persistencia.serializacao.FabricaDeMapeadoresDoCouchDB;

public class BancoDeDocumentos {

	private CouchDbInstance instanciaDoCouch;
	private CouchDbConnector conexaoDoCouch;

	public BancoDeDocumentos(ConfiguracoesProjetos configuracoesProjetos) {
		HttpClient clienteHttp = new StdHttpClient.Builder().build();
		instanciaDoCouch = new StdCouchDbInstance(clienteHttp, new FabricaDeMapeadoresDoCouchDB());
		conexaoDoCouch = instanciaDoCouch.createConnector(configuracoesProjetos.obterNomeDaBaseDeDocumentos(), true);
		System.setProperty("org.ektorp.support.AutoUpdateViewOnChange", "true");
	}

	public void inserirDocumento(Documento documento) {
		conexaoDoCouch.create(documento);
	}

	public void removerDocumento(Documento documento) {
		conexaoDoCouch.delete(documento);
	}

	public RepositorioDeProjetos obterRepositorioDeProjetos() {
		return new RepositorioDeProjetos(conexaoDoCouch);
	}

}
