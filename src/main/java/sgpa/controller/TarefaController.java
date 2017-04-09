package sgpa.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import sgpa.controller.exception.ForbiddenException;
import sgpa.controller.exception.InvalidRequestException;
import sgpa.controller.exception.ResourceNotFoundException;
import sgpa.model.Milestone;
import sgpa.model.Tarefa;
import sgpa.service.MilestoneService;
import sgpa.service.TarefaService;

@Controller
@RequestMapping("projeto/{idProjeto}/tarefa")
public class TarefaController extends ProjetoBaseController {

	private static final String VIEW_TAREFA_LISTAR = "tarefa/listar";

	private TarefaService tarefaService;
	private MilestoneService milestoneService;

	@Autowired
	public TarefaController(TarefaService tarefaService, MilestoneService milestoneService) {
		this.tarefaService = tarefaService;
		this.milestoneService = milestoneService;
	}

	@GetMapping
	public ModelAndView listar(@PathVariable Long idProjeto) {
		ModelAndView mv = new ModelAndView(VIEW_TAREFA_LISTAR);
		mv.addObject("listaMilestone", milestoneService.listarTodosPorProjeto(projeto));
		mv.addObject("responsaveis", projetoService.obterParticipacoesPorProjeto(projeto));
		return mv;
	}

	@GetMapping("obtertarefas")
	@ResponseBody
	public Iterable<Tarefa> todas(@PathVariable Long idProjeto) {
		Iterable<Tarefa> tarefas = tarefaService.listarTodosPorProjeto(projeto);
		return tarefas;
	}

	@GetMapping("obtermilestones")
	@ResponseBody
	public Iterable<Milestone> todos(@PathVariable Long idProjeto) {
		Iterable<Milestone> milestones = milestoneService.listarTodosPorProjeto(projeto);
		return milestones;
	}

	@RequestMapping("cadastrar")
	@ResponseBody
	public Tarefa cadastrar(@PathVariable Long idProjeto, @Valid Tarefa tarefa, BindingResult result) {
		if (result.hasErrors()) {
			if (tarefa.getResponsaveis().size() == 0) {
				result.rejectValue("responsaveis", "Selecione pelo menos um membro", "Selecione pelo menos um membro");
			}
			throw new InvalidRequestException("", result);
		}
		return tarefaService.salvar(tarefa);
	}

	@ResponseBody
	@GetMapping("{idTarefa}/excluir")
	public String excluir(@PathVariable Long idProjeto, @PathVariable Long idTarefa) {
		Tarefa tarefa = verificarSeTarefaExiste(idTarefa);
		verificarSeParticipantePossuiPermissao(tarefa);
		tarefaService.deletar(tarefa);
		return "Excluir Tarefa " + idTarefa + " do Projeto " + idProjeto;
	}

	@ResponseBody
	@GetMapping("{idTarefa}/alterarstatus")
	public Tarefa alterarStatus(@PathVariable Long idProjeto, @PathVariable Long idTarefa) {
		Tarefa tarefa = verificarSeTarefaExiste(idTarefa);
		verificarSeParticipantePossuiPermissao(tarefa);
		return tarefaService.alterarStatus(tarefa);
	}

	private Tarefa verificarSeTarefaExiste(Long idTarefa) {
		Tarefa tarefa = tarefaService.obterPorIdPorProjetoId(idTarefa, projeto.getId());
		if (tarefa != null)
			return tarefa;
		else
			throw new ResourceNotFoundException();
	}

	private void verificarSeParticipantePossuiPermissao(Tarefa tarefa) {
		if (!tarefa.possuiPermissao(participacao.getId()))
			throw new ForbiddenException();
	}

	@ResponseBody
	@GetMapping("{idTarefa}/editar")
	public String editar(@PathVariable Long idProjeto, @PathVariable Long idTarefa) {
		return "Editar Tarefas do Projeto " + idProjeto;

	}

	@ResponseBody
	@PostMapping()
	public String salvar(@PathVariable Long idProjeto) {
		return "Editar Tarefas do Projeto " + idProjeto;

	}
}
