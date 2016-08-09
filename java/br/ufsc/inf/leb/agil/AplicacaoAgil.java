package br.ufsc.inf.leb.agil;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

import br.ufsc.inf.leb.agil.persistencia.serializacao.FabricaDeMapeadoresDoJersey;
import br.ufsc.inf.leb.agil.recursos.RecursoCss;
import br.ufsc.inf.leb.agil.recursos.RecursoFonts;
import br.ufsc.inf.leb.agil.recursos.RecursoGif;
import br.ufsc.inf.leb.agil.recursos.RecursoHtml;
import br.ufsc.inf.leb.agil.recursos.RecursoJs;
import br.ufsc.inf.leb.agil.recursos.RecursoPng;
import br.ufsc.inf.leb.agil.recursos.RecursoProjeto;
import br.ufsc.inf.leb.agil.recursos.RecursoProjetoArquivo;
import br.ufsc.inf.leb.agil.recursos.RecursoProjetoArquivos;
import br.ufsc.inf.leb.agil.recursos.RecursoProjetoArquivosZip;
import br.ufsc.inf.leb.agil.recursos.RecursoProjetoClassePrincipal;
import br.ufsc.inf.leb.agil.recursos.RecursoProjetoExecucao;
import br.ufsc.inf.leb.agil.recursos.RecursoProjetos;
import br.ufsc.inf.leb.agil.recursos.RecursoRaiz;

public class AplicacaoAgil extends ResourceConfig {

	public AplicacaoAgil() {
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
