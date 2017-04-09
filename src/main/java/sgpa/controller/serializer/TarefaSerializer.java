package sgpa.controller.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import sgpa.helper.DataHelper;
import sgpa.model.Participacao;
import sgpa.model.Tarefa;

public class TarefaSerializer extends JsonSerializer<Tarefa> {

	@Override
	public void serialize(Tarefa tarefa, JsonGenerator gen, SerializerProvider serializers)
			throws IOException, JsonProcessingException {

		if (tarefa != null) {
			gen.writeStartObject();
			gen.writeNumberField("id", tarefa.getId());
			gen.writeStringField("titulo", tarefa.getTitulo());
			gen.writeStringField("descricao", tarefa.getDescricao());
			gen.writeStringField("dataInicio",
					(tarefa.getDataInicio() != null) ? DataHelper.formatarDataSimples(tarefa.getDataInicio()) : null);
			gen.writeStringField("dataLimite",
					(tarefa.getDataLimite() != null) ? DataHelper.formatarDataSimples(tarefa.getDataLimite()) : null);
			gen.writeStringField("dataConclusao", (tarefa.getDataConclusao() != null)
					? DataHelper.formatarDataSimples(tarefa.getDataConclusao()) : null);
			gen.writeBooleanField("concluida", tarefa.getConcluida());

			gen.writeArrayFieldStart("responsaveis");
			for (Participacao participacao : tarefa.getResponsaveis()) {
				gen.writeStartObject();
				gen.writeNumberField("id", participacao.getId());
				gen.writeStringField("nome", participacao.getUsuario().getNome());
				gen.writeStringField("tipo", participacao.getTipoParticipacao().getDescricao());
				gen.writeEndObject();
			}
			gen.writeEndArray();

			gen.writeEndObject();
		}

	}

}
