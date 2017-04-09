package sgpa.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import sgpa.controller.exception.ResourceNotFoundException;
import sgpa.controller.page.PageWrapper;
import sgpa.model.Noticia;
import sgpa.repository.filter.NoticiaFilter;
import sgpa.service.NoticiaService;

@Controller
@RequestMapping("noticia")
public class NoticiaController {

	private static final String VIEW_NOTICIA_LISTAR = "noticia/listar";
	private static final String VIEW_NOTICIA_CADASTRAR = "noticia/cadastrar";

	private NoticiaService noticiaService;

	@Autowired
	public NoticiaController(NoticiaService noticiaService) {
		this.noticiaService = noticiaService;
	}

	@GetMapping
	public ModelAndView listar(NoticiaFilter noticiaFilter, @PageableDefault(size = 10) Pageable pageable,
			HttpServletRequest request) {
		PageWrapper<Noticia> paginaWrapper = new PageWrapper<>(noticiaService.filtrar(noticiaFilter, pageable),
				request);

		ModelAndView mv = new ModelAndView(VIEW_NOTICIA_LISTAR);
		mv.addObject("listaCategoriaNoticia", noticiaService.listarTodasCategorias());
		mv.addObject("pagina", paginaWrapper);
		return mv;
	}

	@GetMapping("cadastrar")
	public ModelAndView cadastrar(Noticia noticia) {
		ModelAndView mv = new ModelAndView(VIEW_NOTICIA_CADASTRAR);
		mv.addObject("listaCategoriaNoticia", noticiaService.listarTodasCategorias());
		return mv;
	}

	@GetMapping("/{idNoticia}/editar")
	public ModelAndView editar(@PathVariable("idNoticia") Long idNoticia) {
		ModelAndView mv = new ModelAndView(VIEW_NOTICIA_CADASTRAR);
		Noticia noticia = verificarSeNoticiaExiste(idNoticia);
		mv.addObject("noticia", noticia);
		mv.addObject("listaCategoriaNoticia", noticiaService.listarTodasCategorias());
		return mv;
	}

	@PostMapping("cadastrar")
	public ModelAndView salvar(@Valid Noticia noticia, BindingResult result, RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			return cadastrar(noticia);
		}
		noticiaService.salvar(noticia);
		redirectAttributes.addFlashAttribute("mensagem", "Noticia salva com sucesso");
		return new ModelAndView("redirect:/noticia");
	}

	private Noticia verificarSeNoticiaExiste(Long idNoticia) {
		Noticia noticia = noticiaService.obterPorId(idNoticia);
		if (noticia != null)
			return noticia;
		else
			throw new ResourceNotFoundException();
	}

}
