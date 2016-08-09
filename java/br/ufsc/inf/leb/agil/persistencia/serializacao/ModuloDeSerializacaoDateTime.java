package br.ufsc.inf.leb.projetos.persistencia.serializacao;

import org.joda.time.DateTime;

import com.fasterxml.jackson.databind.module.SimpleModule;

public class ModuloDeSerializacaoDateTime extends SimpleModule {

	private static final long serialVersionUID = -6702790610025748391L;

	public ModuloDeSerializacaoDateTime() {
		addDeserializer(DateTime.class, new DeserializadorDateTime());
		addSerializer(DateTime.class, new SerializadorDateTime());
	}

}
