package br.ufsc.inf.leb.projetos.persistencia.serializacao;

import java.io.IOException;

import org.joda.time.DateTime;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class SerializadorDateTime extends JsonSerializer<DateTime> {

	@Override
	public void serialize(DateTime valor, JsonGenerator json, SerializerProvider provider) throws IOException, JsonGenerationException {
		json.writeString(valor.toString());
	}

}
