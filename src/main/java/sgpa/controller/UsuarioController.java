package sgpa.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import sgpa.controller.exception.ResourceNotFoundException;
import sgpa.controller.page.PageWrapper;
import sgpa.controller.page.RespostaSugestao;
import sgpa.controller.page.Sugestao;
import sgpa.model.GrupoUsuario;
import sgpa.model.TokenRecuperacaoSenha;
import sgpa.model.TokenVerificacao;
import sgpa.model.Usuario;
import sgpa.repository.filter.UsuarioFilter;
import sgpa.security.PrimeiroAcesso;
import sgpa.service.DepartamentoService;
import sgpa.service.MessageService;
import sgpa.service.TokenService;
import sgpa.service.UsuarioService;
import sgpa.service.exception.TokenRecuperacaoSenhaNaoEncontrado;
import sgpa.service.exception.TokenVerificacaoNaoEncontradoException;
import sgpa.service.exception.UsuarioJaCadastradoException;
import sgpa.service.exception.UsuarioNaoExistenteException;

@Controller
@RequestMapping("usuario")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private DepartamentoService departamentoService;

	@Autowired
	private TokenService tokenService;

	@Autowired
	private MessageService ms;

	@RequestMapping("")
	public ModelAndView principal() {
		return cadastrar(new Usuario());
	}

	@RequestMapping("login")
	public ModelAndView login(@AuthenticationPrincipal User user) {
		if (user != null) {
			return new ModelAndView("redirect:/usuario");
		}

		return new ModelAndView("usuario/login");
	}

	@RequestMapping("primeiroacesso")
	public ModelAndView primeiroAcesso(@RequestParam("token") String token, PrimeiroAcesso primeiroAcesso) {
		ModelAndView mv = new ModelAndView("usuario/primeiroacesso");
		mv.addObject("token", token);
		try {
			TokenVerificacao tv = tokenService.buscarTokenVerificacaoPorToken(token);
			if (tv.getUsuario().getEmailConfirmado()) {
				return new ModelAndView("redirect:/usuario/login");
			}

		} catch (TokenVerificacaoNaoEncontradoException e) {
			return new ModelAndView("redirect:/usuario/login");
		}
		return mv;
	}

	@RequestMapping(value = "definirsenha", method = RequestMethod.POST)
	public ModelAndView definirSenha(@Valid PrimeiroAcesso primeiroAcesso, BindingResult result,
			RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			return primeiroAcesso(primeiroAcesso.getToken(), primeiroAcesso);
		}
		if (!primeiroAcesso.eIgual()) {
			result.rejectValue("senha", ms.getMessage("form.firstaccess.message.erro"),
					ms.getMessage("form.firstaccess.message.erro"));
			return primeiroAcesso(primeiroAcesso.getToken(), primeiroAcesso);
		}
		try {
			TokenVerificacao tv = tokenService.buscarTokenVerificacaoPorToken(primeiroAcesso.getToken());
			usuarioService.definirSenhaPrimeiroAcesso(tv, primeiroAcesso);
			redirectAttributes.addFlashAttribute("mensagem", ms.getMessage("form.firstaccess.message.success"));
			return new ModelAndView("redirect:/usuario/login");

		} catch (TokenVerificacaoNaoEncontradoException e) {
			return new ModelAndView("redirect:/usuario/login");
		}
	}

	@RequestMapping(value = "recuperarsenha", method = RequestMethod.GET)
	public ModelAndView esqueciminhasenha() {
		return new ModelAndView("usuario/recuperarsenha");
	}

	@RequestMapping(value = "recuperarsenha", method = RequestMethod.POST)
	public ModelAndView recuperarsenha(@RequestParam("email") String email, RedirectAttributes redirectAttributes) {
		try {
			Usuario usuario = usuarioService.obterPorEmail(email);
			tokenService.gerarTokenRecuperacaoSenha(usuario);
		} catch (UsuarioNaoExistenteException e) {
			return new ModelAndView("redirect:/usuario/login");
		}
		redirectAttributes.addAttribute("mensagem", "Você vai receber um email com link para modificação da senha");
		return new ModelAndView("redirect:/usuario/login");

	}

	@RequestMapping("alterarsenha")
	public ModelAndView alterarsenha(@RequestParam("token") String token, PrimeiroAcesso primeiroAcesso) {
		try {
			tokenService.buscarTokenRecuparacaoSenhaPorToken(token);
			ModelAndView mv = new ModelAndView("usuario/alterarsenha");
			mv.addObject("token", token);
			return mv;
		} catch (TokenRecuperacaoSenhaNaoEncontrado e) {
			return new ModelAndView("redirect:/usuario/login");
		}

	}

	@RequestMapping(value = "alterarsenha", method = RequestMethod.POST)
	public ModelAndView alterarsenha(@Valid PrimeiroAcesso primeiroAcesso, BindingResult result,
			RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			return alterarsenha(primeiroAcesso.getToken(), primeiroAcesso);
		}
		if (!primeiroAcesso.eIgual()) {
			result.rejectValue("senha", "Confirmação Senha deve ser o mesmo que Senha",
					"Confirmação Senha deve ser o mesmo que Senha");
			return alterarsenha(primeiroAcesso.getToken(), primeiroAcesso);
		}
		try {
			TokenRecuperacaoSenha tv = tokenService.buscarTokenRecuparacaoSenhaPorToken(primeiroAcesso.getToken());
			usuarioService.redefinirSenha(tv, primeiroAcesso);
			redirectAttributes.addFlashAttribute("mensagem", "Você redefiniu sua senha com sucesso. Realize o login.");
			return new ModelAndView("redirect:/usuario/login");

		} catch (TokenRecuperacaoSenhaNaoEncontrado e) {
			return new ModelAndView("redirect:/usuario/login");
		}
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView listar(UsuarioFilter usuarioFilter, @PageableDefault(size = 10) Pageable pageable,
			HttpServletRequest httpServletRequest) {
		PageWrapper<Usuario> paginaWrapper = new PageWrapper<>(usuarioService.filtrar(usuarioFilter, pageable),
				httpServletRequest);

		ModelAndView mv = new ModelAndView("usuario/listar");
		mv.addObject("pagina", paginaWrapper);
		System.out.println(pageable.getPageSize());
		System.out.println(pageable.getPageNumber());
		System.out.println(pageable.getOffset());

		return mv;
	}

	@RequestMapping(value = "cadastrar", method = RequestMethod.GET)
	public ModelAndView cadastrar(Usuario usuario) {
		ModelAndView mv = new ModelAndView("usuario/cadastrar");
		mv.addObject("grupos", GrupoUsuario.values());
		mv.addObject("departamentos", departamentoService.listarTodos());
		return mv;
	}

	@RequestMapping(value = "cadastrar", method = RequestMethod.POST)
	public ModelAndView salvar(@Valid Usuario usuario, BindingResult result, RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			return cadastrar(usuario);
		}

		if (usuario.getId() != null) {
			verificarSeUsuarioExiste(usuario.getId());
			usuarioService.editar(usuario);
			redirectAttributes.addFlashAttribute("mensagem", ms.getMessage("form.user.register.sucessmessage"));
			return new ModelAndView("redirect:/usuario");
		}

		try {
			usuarioService.cadastrar(usuario);
			redirectAttributes.addFlashAttribute("mensagem", ms.getMessage("form.user.register.sucessmessage"));
			return new ModelAndView("redirect:/usuario");
		} catch (UsuarioJaCadastradoException e) {
			result.rejectValue("email", e.getMessage(), e.getMessage());
			return cadastrar(usuario);
		}
	}

	@GetMapping("{idUsuario}/editar")
	public ModelAndView editar(@PathVariable("idUsuario") Long idUsuario) {
		Usuario usuario = verificarSeUsuarioExiste(idUsuario);
		ModelAndView mv = new ModelAndView("usuario/cadastrar");
		mv.addObject("grupos", GrupoUsuario.values());
		mv.addObject("departamentos", departamentoService.listarTodos());
		mv.addObject("usuario", usuario);
		return mv;
	}

	@ResponseBody
	@GetMapping("listarpornome")
	public RespostaSugestao obterParaAutoComplete(@RequestParam(name = "nome", defaultValue = "") String nome) {

		List<Usuario> usuarios = usuarioService.listarTodosPorNome(nome);
		List<Sugestao> sugestoes = new ArrayList<>();
		Sugestao s;
		for (Usuario usuario : usuarios) {
			s = new Sugestao(usuario.getNome(), usuario.getId().toString());
			sugestoes.add(s);
		}
		return new RespostaSugestao(sugestoes);
	}

	private Usuario verificarSeUsuarioExiste(Long idUsuario) {
		Usuario usuario = usuarioService.obterPorId(idUsuario);
		if (usuario != null)
			return usuario;
		else
			throw new ResourceNotFoundException();
	}

}
