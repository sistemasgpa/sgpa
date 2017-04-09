package sgpa.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import sgpa.controller.exception.ResourceNotFoundException;
import sgpa.model.Milestone;
import sgpa.service.MilestoneService;
import sgpa.service.exception.DataLimiteMenorQueDataInicioException;

@Controller
@RequestMapping("projeto/{idProjeto}/milestone")
public class MilestoneController extends ProjetoBaseController {

	private static final String VIEW_MILESTONE_LISTAR = "milestone/listar";
	private static final String VIEW_MILESTONE_CADASTRAR = "milestone/cadastrar";

	private MilestoneService milestoneService;

	@Autowired
	public MilestoneController(MilestoneService milestoneService) {
		this.milestoneService = milestoneService;
	}

	@GetMapping
	public ModelAndView listar(@PathVariable Long idProjeto) {
		ModelAndView mv = new ModelAndView(VIEW_MILESTONE_LISTAR);
		return mv;
	}

	@GetMapping("cadastrar")
	public ModelAndView cadastrar(Milestone milestone, BindingResult result) {
		usuarioLogadoECoordenador();
		ModelAndView mv = new ModelAndView(VIEW_MILESTONE_CADASTRAR);
		return mv;
	}

	@GetMapping("{idMilestone}/editar")
	public ModelAndView editar(@PathVariable Long idMilestone) {
		Milestone milestone = verificarSeMilestoneExiste(idMilestone);
		ModelAndView mv = new ModelAndView(VIEW_MILESTONE_CADASTRAR);
		mv.addObject("milestone", milestone);
		return mv;
	}

	@PostMapping()
	public ModelAndView salvar(@Valid Milestone milestone, BindingResult result,
			RedirectAttributes redirectAttributes) {
		usuarioLogadoECoordenador();
		if (result.hasErrors())
			return cadastrar(milestone, result);

		try {
			milestoneService.salvar(milestone);
		} catch (DataLimiteMenorQueDataInicioException e) {
			result.rejectValue("", e.getMessage(), e.getMessage());
			return cadastrar(milestone, result);
		}
		return redirecionarParaModulo("milestone", ms.getMessage("form.milestone.register.message.success"),
				redirectAttributes);
	}

	private Milestone verificarSeMilestoneExiste(Long idMilestone) {
		Milestone milestone = milestoneService.obterPorIdPorProjetoId(idMilestone, projeto.getId());
		if (milestone != null)
			return milestone;
		else
			throw new ResourceNotFoundException();
	}
}
