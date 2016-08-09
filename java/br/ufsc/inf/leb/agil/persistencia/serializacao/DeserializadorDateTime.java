package br.ufsc.inf.leb.projetos.persistencia.serializacao;

import java.io.IOException;

import org.joda.time.DateTime;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class DeserializadorDateTime extends JsonDeserializer<DateTime> {

	@Override
	public DateTime deserialize(JsonParser parser, DeserializationContext contexto) throws IOException, JsonProcessingException {
		return DateTime.parse(parser.getText());
	}

}
