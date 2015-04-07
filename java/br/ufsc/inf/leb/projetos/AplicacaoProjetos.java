package br.ufsc.inf.leb.projetos;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

import br.ufsc.inf.leb.projetos.persistencia.serializacao.FabricaDeMapeadoresDoJersey;
import br.ufsc.inf.leb.projetos.recursos.RecursoCss;
import br.ufsc.inf.leb.projetos.recursos.RecursoFonts;
import br.ufsc.inf.leb.projetos.recursos.RecursoGif;
import br.ufsc.inf.leb.projetos.recursos.RecursoHtml;
import br.ufsc.inf.leb.projetos.recursos.RecursoJs;
import br.ufsc.inf.leb.projetos.recursos.RecursoPng;
import br.ufsc.inf.leb.projetos.recursos.RecursoProjeto;
import br.ufsc.inf.leb.projetos.recursos.RecursoProjetoArquivo;
import br.ufsc.inf.leb.projetos.recursos.RecursoProjetoArquivos;
import br.ufsc.inf.leb.projetos.recursos.RecursoProjetoArquivosZip;
import br.ufsc.inf.leb.projetos.recursos.RecursoProjetoClassePrincipal;
import br.ufsc.inf.leb.projetos.recursos.RecursoProjetoExecucao;
import br.ufsc.inf.leb.projetos.recursos.RecursoProjetos;
import br.ufsc.inf.leb.projetos.recursos.RecursoRaiz;

public class AplicacaoProjetos extends ResourceConfig {

	public AplicacaoProjetos() {
		register(RecursoCss.class);
		register(RecursoHtml.class);
		register(RecursoFonts.class);
		register(RecursoGif.class);
		register(RecursoJs.class);
		register(RecursoProjeto.class);
		register(RecursoProjetoArquivo.class);
		register(RecursoProjetoArquivos.class);
		register(RecursoProjetoArquivosZip.class);
		register(RecursoProjetoClassePrincipal.class);
		register(RecursoProjetoExecucao.class);
		register(RecursoProjetos.class);
		register(RecursoPng.class);
		register(RecursoRaiz.class);
		register(FabricaDeMapeadoresDoJersey.class);
		register(MultiPartFeature.class);
	}

}
