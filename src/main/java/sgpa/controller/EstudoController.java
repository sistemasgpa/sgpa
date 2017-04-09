package sgpa.controller;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import sgpa.controller.exception.ForbiddenException;
import sgpa.controller.exception.ResourceNotFoundException;
import sgpa.model.Estudo;
import sgpa.model.Participacao;
import sgpa.model.Projeto;
import sgpa.model.TipoPublicacao;
import sgpa.service.EstudoService;
import sgpa.service.ProjetoService;

@Controller
@RequestMapping("projeto/{idProjeto}/estudo")
public class EstudoController {

	private ProjetoService projetoService;
	private EstudoService estudoService;

	private Projeto projeto;
	private Participacao participacao;

	@Autowired
	public EstudoController(ProjetoService projetoService, EstudoService estudoService) {
		this.projetoService = projetoService;
		this.estudoService = estudoService;
	}

	@ModelAttribute
	public void carregarDependencias(@PathVariable Long idProjeto, Model model, Principal principal) {
		projeto = projetoService.obterPorId(idProjeto);
		participacao = projetoService.obterParticipacaoProjeto(principal.getName(), idProjeto);
		model.addAttribute("projeto", projeto);
		model.addAttribute("participacao", participacao);
	}

	@GetMapping
	public ModelAndView listar() {
		ModelAndView mv = new ModelAndView("estudo/listar");
		Iterable<Estudo> estudos = estudoService.listarTodosPorProjeto(projeto);
		mv.addObject("estudos", estudos);
		return mv;
	}

	@GetMapping("{idEstudo}")
	public ModelAndView ver(@PathVariable Long idEstudo) {
		ModelAndView mv = new ModelAndView("estudo/ver");
		Estudo estudoEncontrado = verificarSeEstudoExiste(idEstudo);
		mv.addObject("estudo", estudoEncontrado);
		return mv;
	}

	@GetMapping("cadastrar")
	public ModelAndView cadastrar(Estudo estudo, BindingResult result) {
		ModelAndView mv = new ModelAndView("estudo/cadastrar");
		mv.addObject("listaTipoPublicacao", TipoPublicacao.values());
		return mv;
	}

	@GetMapping("{idEstudo}/editar")
	public ModelAndView editar(@PathVariable Long idEstudo) {
		Estudo estudoEncontrado = verificarSeEstudoExiste(idEstudo);
		verificarSeParticipantePossuiPermissao(estudoEncontrado);
		ModelAndView mv = new ModelAndView("estudo/cadastrar");
		mv.addObject("listaTipoPublicacao", TipoPublicacao.values());
		mv.addObject("estudo", estudoEncontrado);
		return mv;
	}

	@PostMapping()
	public ModelAndView salvar(@Valid Estudo estudo, BindingResult result, RedirectAttributes redirectAttributes) {
		if (estudo.getId() == null) {
			estudo.setParticipacao(participacao);
		} else {
			Estudo estudoEncontrado = verificarSeEstudoExiste(estudo.getId());
			verificarSeParticipantePossuiPermissao(estudoEncontrado);
		}

		if (result.hasErrors()) {
			return cadastrar(estudo, result);
		}

		estudoService.salvar(estudo);

		redirectAttributes.addFlashAttribute("mensagem", "O estudo foi salvo.");
		return new ModelAndView("redirect:/projeto/" + projeto.getId() + "/estudo");

	}

	@GetMapping("{idEstudo}/excluir")
	public ModelAndView excluir(@PathVariable Long idEstudo, RedirectAttributes redirectAttributes) {
		Estudo estudoEncontrado = verificarSeEstudoExiste(idEstudo);
		verificarSeParticipantePossuiPermissao(estudoEncontrado);

		estudoService.deletar(estudoEncontrado);

		redirectAttributes.addFlashAttribute("mensagem", "O estudo foi excluido.");
		return new ModelAndView("redirect:/projeto/" + projeto.getId() + "/estudo");
	}

	private Estudo verificarSeEstudoExiste(Long idEstudo) {
		Estudo estudoVerificacao = estudoService.obterPorIdPorProjetoId(idEstudo, projeto.getId());
		if (estudoVerificacao != null)
			return estudoVerificacao;
		else
			throw new ResourceNotFoundException();
	}

	private void verificarSeParticipantePossuiPermissao(Estudo estudo) {
		if (!estudo.getParticipacao().getId().equals(participacao.getId()))
			if (!participacao.isCoordenador())
				throw new ForbiddenException();
	}

}
