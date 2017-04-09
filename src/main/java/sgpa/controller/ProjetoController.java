package sgpa.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import sgpa.controller.exception.ForbiddenException;
import sgpa.controller.exception.ResourceNotFoundException;
import sgpa.controller.page.PageWrapper;
import sgpa.model.Projeto;
import sgpa.model.StatusProjeto;
import sgpa.model.TipoParticipacao;
import sgpa.repository.filter.ProjetoFilter;
import sgpa.service.CategoriaProjetoService;
import sgpa.service.ProjetoService;
import sgpa.service.exception.ProjetoNaoPossuiCoordenadorException;
import sgpa.service.exception.ProjetoNaoPossuiParticipantesException;

@Controller
@RequestMapping("projeto")
public class ProjetoController {

	private static final String VIEW_PROJETO_CRIAR_EDITAR = "projeto/cadastrar";
	private static final String VIEW_PROJETO_LISTAR = "projeto/listar";

	private final ProjetoService projetoService;
	private final CategoriaProjetoService categoriaProjetoService;

	@Autowired
	public ProjetoController(ProjetoService projetoService, CategoriaProjetoService categoriaProjetoService) {
		this.projetoService = projetoService;
		this.categoriaProjetoService = categoriaProjetoService;
	}

	@ModelAttribute
	public void carregarDependencias(Model model) {
		model.addAttribute("listaStatusProjeto", StatusProjeto.values());
		model.addAttribute("listaTipoParticipacao", TipoParticipacao.values());
		model.addAttribute("listaCategoriaProjeto", categoriaProjetoService.listarTodos());
	}

	@GetMapping
	public ModelAndView listar(ProjetoFilter projetoFilter, @PageableDefault(size = 10) Pageable pageable,
			HttpServletRequest request) {
		PageWrapper<Projeto> paginaWrapper = new PageWrapper<>(projetoService.filtrar(projetoFilter, pageable),
				request);
		ModelAndView mv = new ModelAndView(VIEW_PROJETO_LISTAR);

		mv.addObject("pagina", paginaWrapper);
		return mv;
	}

	@GetMapping("cadastrar")
	public ModelAndView cadastrar(Projeto projeto) {
		ModelAndView mv = new ModelAndView(VIEW_PROJETO_CRIAR_EDITAR);
		return mv;
	}

	@GetMapping("{idProjeto}/editar")
	public ModelAndView editar(@PathVariable("idProjeto") Long idProjeto, Principal principal,
			HttpServletRequest request) {
		Projeto projeto = projetoService.obterPorId(idProjeto);
		if (projeto == null)
			throw new ResourceNotFoundException();

		possuiPermissao(projeto, request);

		ModelAndView mv = new ModelAndView(VIEW_PROJETO_CRIAR_EDITAR);
		mv.addObject("projeto", projeto);
		return mv;
	}

	@PostMapping("cadastrar")
	@ResponseBody
	public ModelAndView salvar(@Valid Projeto projeto, BindingResult result, RedirectAttributes redirectAttributes,
			HttpServletRequest request) {

		if (projeto.getId() != null) {
			Projeto projetoEncontrado = projetoService.obterPorId(projeto.getId());
			possuiPermissao(projetoEncontrado, request);
		}

		if (result.hasErrors()) {
			return cadastrar(projeto);
		}

		try {
			projeto = projetoService.salvar(projeto);
		} catch (ProjetoNaoPossuiParticipantesException e) {
			result.rejectValue("", e.getMessage(), e.getMessage());
			return cadastrar(projeto);
		} catch (ProjetoNaoPossuiCoordenadorException e) {
			result.rejectValue("", e.getMessage(), e.getMessage());
			return cadastrar(projeto);
		}
		redirectAttributes.addFlashAttribute("mensagem", "O Projeto foi salvo com sucesso.");
		return new ModelAndView("redirect:/projeto");
	}

	public void possuiPermissao(Projeto projeto, HttpServletRequest request) {
		if (request.isUserInRole("PADRAO")) {
			if (!projeto.isCoordenador(request.getUserPrincipal().getName())) {
				throw new ForbiddenException();
			}
		}
	}

}
