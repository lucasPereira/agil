package br.ufsc.inf.leb.projetos.persistencia.serializacao;

import org.ektorp.CouchDbConnector;
import org.ektorp.impl.StdObjectMapperFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class FabricaDeMapeadoresDoCouchDB extends StdObjectMapperFactory {

	@Override
	public ObjectMapper createObjectMapper(CouchDbConnector connector) {
		ObjectMapper mapeador = super.createObjectMapper(connector);
		mapeador.registerModule(new ModuloDeSerializacaoDateTime());
		return mapeador;
	}

}
