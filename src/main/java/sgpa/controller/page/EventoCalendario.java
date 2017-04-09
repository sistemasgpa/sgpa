package sgpa.controller.page;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import sgpa.model.Evento;

public class EventoCalendario {

	private Long id;
	private String title;
	private String start;
	private String end;
	private String url;

	@JsonIgnore
	private List<EventoCalendario> eventos;

	public EventoCalendario() {
	}

	public EventoCalendario(Long id, String title, String start, String end, String url) {
		this.id = id;
		this.title = title;
		this.start = start;
		this.end = end;
		this.url = url;
	}

	public EventoCalendario(List<Evento> eventos) {
		this.eventos = new ArrayList<EventoCalendario>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (Evento evento : eventos) {
			this.eventos.add(new EventoCalendario(evento.getId(), evento.getTitulo(),
					sdf.format(evento.getDataInicio()), sdf.format(evento.getDataFim()), ""));
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<EventoCalendario> getEventos() {
		return eventos;
	}

	public void setEventos(List<EventoCalendario> eventos) {
		this.eventos = eventos;
	}

}
