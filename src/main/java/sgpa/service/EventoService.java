package sgpa.service;

import java.util.List;

import org.springframework.stereotype.Service;

import sgpa.model.Evento;
import sgpa.model.Projeto;
import sgpa.repository.EventoRepository;

@Service
public class EventoService {

	private EventoRepository eventoRepository;

	public EventoService(EventoRepository eventoRepository) {
		this.eventoRepository = eventoRepository;
	}

	public List<Evento> listarTodosPorProjeto(Projeto projeto) {
		return eventoRepository.findByProjeto(projeto);
	}

	public Evento salvar(Evento evento) {
		return eventoRepository.save(evento);
	}

}
