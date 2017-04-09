package sgpa.controller.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import sgpa.helper.DataHelper;
import sgpa.model.Evento;

public class EventoSerializer extends JsonSerializer<Evento> {

	@Override
	public void serialize(Evento evento, JsonGenerator gen, SerializerProvider serializers)
			throws IOException, JsonProcessingException {

		if (evento != null) {
			gen.writeStartObject();
			gen.writeNumberField("id", evento.getId());
			gen.writeStringField("titulo", evento.getTitulo());
			gen.writeStringField("descricao", evento.getDescricao());
			gen.writeStringField("dataInicio",
					(evento.getDataInicio() != null) ? DataHelper.formatarData(evento.getDataInicio()) : null);
			gen.writeStringField("dataFim",
					(evento.getDataFim() != null) ? DataHelper.formatarData(evento.getDataFim()) : null);
			gen.writeEndObject();
		}

	}

}
