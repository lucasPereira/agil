package br.ufsc.inf.leb.projetos.persistencia.serializacao;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.ObjectMapper;

@Provider
public class FabricaDeMapeadoresDoJersey implements ContextResolver<ObjectMapper> {

	@Override
	public ObjectMapper getContext(Class<?> argumento) {
		ObjectMapper mapeador = new ObjectMapper();
		mapeador.registerModule(new ModuloDeSerializacaoDateTime());
		return mapeador;
	}

}
