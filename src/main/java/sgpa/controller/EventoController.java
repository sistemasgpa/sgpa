package sgpa.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import sgpa.controller.exception.InvalidRequestException;
import sgpa.controller.page.EventoCalendario;
import sgpa.model.Evento;
import sgpa.service.EventoService;

@Controller
@RequestMapping("projeto/{idProjeto}/evento")
public class EventoController extends ProjetoBaseController {

	private EventoService eventoService;

	@Autowired
	public EventoController(EventoService eventoService) {
		this.eventoService = eventoService;
	}

	@GetMapping
	public ModelAndView calendario() {
		ModelAndView mv = new ModelAndView("evento/calendario");
		return mv;
	}

	@GetMapping("buscarTodos")
	@ResponseBody
	public List<EventoCalendario> buscarTodos(@PathVariable Long idProjeto) {
		List<Evento> eventos = eventoService.listarTodosPorProjeto(projeto);
		EventoCalendario eventoCalendario = new EventoCalendario(eventos);
		return eventoCalendario.getEventos();
	}

	@PostMapping("cadastrar")
	@ResponseBody
	public Evento editarEvento(@Valid Evento evento, BindingResult result) {
		if (result.hasErrors()) {
			throw new InvalidRequestException("", result);
		}
		return eventoService.salvar(evento);

	}

	@PostMapping("editar")
	@ResponseBody
	public void editarEvento(@RequestBody EventoCalendario eventoCalendario, HttpServletResponse response) {
		System.out.println(eventoCalendario.getId());
		System.out.println(eventoCalendario.getStart());
		System.out.println(eventoCalendario.getEnd());

		ModelAndView mv = new ModelAndView();

		if (eventoCalendario.getId().equals(1L))
			mv.setStatus(HttpStatus.OK);

		if (eventoCalendario.getId().equals(2L))
			mv.setStatus(HttpStatus.FORBIDDEN);

		if (eventoCalendario.getId().equals(3L))
			mv.setStatus(HttpStatus.BAD_REQUEST);

	}

}
