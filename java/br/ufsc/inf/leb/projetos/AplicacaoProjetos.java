package br.ufsc.inf.leb.projetos;

import org.glassfish.jersey.server.ResourceConfig;

import br.ufsc.inf.leb.projetos.persistencia.serializacao.FabricaDeMapeadoresDoJersey;
import br.ufsc.inf.leb.projetos.recursos.RecursoCss;
import br.ufsc.inf.leb.projetos.recursos.RecursoFonts;
import br.ufsc.inf.leb.projetos.recursos.RecursoHtml;
import br.ufsc.inf.leb.projetos.recursos.RecursoJs;
import br.ufsc.inf.leb.projetos.recursos.RecursoProjeto;
import br.ufsc.inf.leb.projetos.recursos.RecursoProjetos;
import br.ufsc.inf.leb.projetos.recursos.RecursoRaiz;

public class AplicacaoProjetos extends ResourceConfig {

	public AplicacaoProjetos() {
		register(RecursoCss.class);
		register(RecursoHtml.class);
		register(RecursoFonts.class);
		register(RecursoJs.class);
		register(RecursoProjeto.class);
		register(RecursoProjetos.class);
		register(RecursoRaiz.class);
		register(FabricaDeMapeadoresDoJersey.class);
	}

}
