package sgpa.controller;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import sgpa.controller.exception.ResourceNotFoundException;
import sgpa.controller.page.FormularioContato;
import sgpa.controller.page.PageWrapper;
import sgpa.model.Noticia;
import sgpa.model.Projeto;
import sgpa.model.StatusProjeto;
import sgpa.model.Usuario;
import sgpa.repository.filter.ProjetoFilter;
import sgpa.service.CategoriaProjetoService;
import sgpa.service.EmailService;
import sgpa.service.MessageService;
import sgpa.service.NoticiaService;
import sgpa.service.ProjetoService;

@Controller
public class PlataformaController {

	private static final String VIEW_PLATAFORMA_INDEX = "plataforma/index";
	private static final String VIEW_PLATAFORMA_SOBRE = "plataforma/sobre";
	private static final String VIEW_PLATAFORMA_PROJETOS = "plataforma/projetos";
	private static final String VIEW_PLATAFORMA_DETALHE_PROJETO = "plataforma/detalhe_projeto";
	private static final String VIEW_PLATAFORMA_NOTICIAS = "plataforma/noticias";
	private static final String VIEW_PLATAFORMA_DETALHE_NOTICIA = "plataforma/detalhe_noticia";
	private static final String VIEW_PLATAFORMA_CONTATO = "plataforma/contato";

	private ProjetoService projetoService;
	private CategoriaProjetoService categoriaProjetoService;
	private NoticiaService noticiaService;
	private EmailService emailService;
	private MessageService ms;

	@Autowired
	public PlataformaController(ProjetoService projetoService, CategoriaProjetoService categoriaProjetoService,
			NoticiaService noticiaService, EmailService emailService, MessageService ms) {
		this.projetoService = projetoService;
		this.categoriaProjetoService = categoriaProjetoService;
		this.noticiaService = noticiaService;
		this.emailService = emailService;
		this.ms = ms;
	}

	@GetMapping("/")
	public ModelAndView index() {
		return new ModelAndView("redirect:/projeto");
	}

	@GetMapping("/site")
	public ModelAndView siteIndex() {
		Page<Projeto> pageProjeto = projetoService.listarTodosVisiveis(new PageRequest(0, 2, Direction.DESC, "id"));
		Page<Noticia> pageNoticia = noticiaService.listarTodas(new PageRequest(0, 4, Direction.DESC, "data"));
		ModelAndView mv = new ModelAndView(VIEW_PLATAFORMA_INDEX);
		mv.addObject("projetos", pageProjeto.getContent());
		mv.addObject("noticias", pageNoticia.getContent());
		return mv;
	}

	@GetMapping("/site/sobre")
	public ModelAndView sobre() {
		ModelAndView mv = new ModelAndView(VIEW_PLATAFORMA_SOBRE);
		return mv;
	}

	@GetMapping("/site/projetos")
	public ModelAndView projetos(ProjetoFilter projetoFilter, @PageableDefault(size = 10) Pageable pageable,
			HttpServletRequest request) {
		PageWrapper<Projeto> paginaWrapper = new PageWrapper<>(projetoService.filtrar(projetoFilter, pageable),
				request);
		ModelAndView mv = new ModelAndView(VIEW_PLATAFORMA_PROJETOS);
		mv.addObject("listaCategoriaProjeto", categoriaProjetoService.listarTodos());
		mv.addObject("listaStatusProjeto", StatusProjeto.values());
		mv.addObject("pagina", paginaWrapper);
		return mv;
	}

	@GetMapping("/site/projetos/{codigoProjeto}")
	public ModelAndView detalheProjeto(@PathVariable Long codigoProjeto, FormularioContato formularioContato) {
		Projeto projeto = projetoService.obterPorId(codigoProjeto);
		if (projeto == null) {
			throw new ResourceNotFoundException();
		}
		ModelAndView mv = new ModelAndView(VIEW_PLATAFORMA_DETALHE_PROJETO);
		mv.addObject("projeto", projeto);
		return mv;
	}

	@PostMapping("/site/projetos/{codigoProjeto}")
	public ModelAndView enviarContatoProjeto(@PathVariable Long codigoProjeto,
			@Valid FormularioContato formularioContato, BindingResult result, RedirectAttributes redirectAttributes) {
		Projeto projeto = projetoService.obterPorId(codigoProjeto);
		if (projeto == null) {
			throw new ResourceNotFoundException();
		}

		if (result.hasErrors()) {
			return detalheProjeto(codigoProjeto, formularioContato);
		}
		String assunto = "Contato Relacionado ao Projeto: " + projeto.getTitulo();

		try {
			for (Usuario coordenador : projeto.obterCoordenadores()) {
				emailService.enviar(coordenador.getEmail(), assunto, formularioContato.getTexto());
			}
		} catch (MessagingException e) {

		}
		redirectAttributes.addFlashAttribute("mensagem",
				"O contato foi enviado. Aguarde a resposta de um dos coordenadores do projeto.");
		return new ModelAndView("redirect:/site/projetos/" + codigoProjeto);
	}

	@GetMapping("/site/noticias")
	public ModelAndView noticias(@RequestParam(name = "categoria", required = false) Long idCategoria,
			@PageableDefault(size = 10) Pageable pageable, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView(VIEW_PLATAFORMA_NOTICIAS);
		if (idCategoria == null)
			idCategoria = 0L;
		PageWrapper<Noticia> paginaWrapper = new PageWrapper<>(
				noticiaService.filtrarPorIdCategoria(idCategoria, pageable), request);
		mv.addObject("listaCategoriaNoticia", noticiaService.listarTodasCategorias());
		mv.addObject("pagina", paginaWrapper);
		return mv;
	}

	@GetMapping("/site/noticias/{codigoNoticia}")
	public ModelAndView detalheNoticia(@PathVariable Long codigoNoticia) {
		Noticia noticia = noticiaService.obterPorId(codigoNoticia);
		if (noticia == null)
			throw new ResourceNotFoundException();

		ModelAndView mv = new ModelAndView(VIEW_PLATAFORMA_DETALHE_NOTICIA);
		mv.addObject("noticia", noticia);
		return mv;
	}

	@GetMapping("/site/contato")
	public ModelAndView contato(FormularioContato formularioContato) {
		ModelAndView mv = new ModelAndView(VIEW_PLATAFORMA_CONTATO);
		return mv;
	}

	@PostMapping("/site/contato")
	public ModelAndView enviarContatoSite(@Valid FormularioContato formularioContato, BindingResult result,
			RedirectAttributes redirectAttributes) {
		if (result.hasErrors())
			return contato(formularioContato);

		String assunto = "Contato Plataforma Projetos";
		try {
			emailService.enviar(ms.getMessage("site.admin.email"), assunto, formularioContato.getTexto());
		} catch (MessagingException e) {

		}
		redirectAttributes.addFlashAttribute("mensagem",
				"O contato foi enviado. Aguarde a resposta de um dos coordenadores do projeto.");
		return new ModelAndView("redirect:/site/contato");
	}

}
