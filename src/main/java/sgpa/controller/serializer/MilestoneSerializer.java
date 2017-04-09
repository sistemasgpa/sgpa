package sgpa.controller.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import sgpa.model.Milestone;
import sgpa.model.Tarefa;

public class MilestoneSerializer extends JsonSerializer<Milestone> {

	@Override
	public void serialize(Milestone milestone, JsonGenerator gen, SerializerProvider serializers)
			throws IOException, JsonProcessingException {

		if (milestone != null) {
			gen.writeStartObject();
			gen.writeNumberField("id", milestone.getId());
			gen.writeStringField("titulo", milestone.getTitulo());
			gen.writeStringField("descricao", milestone.getDescricao());

			gen.writeArrayFieldStart("tarefas");
			for (Tarefa tarefa : milestone.getTarefas()) {
				gen.writeObject(tarefa);
			}
			gen.writeEndArray();

			gen.writeEndObject();
		}

	}

}
