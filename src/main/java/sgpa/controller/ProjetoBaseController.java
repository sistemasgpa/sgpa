package sgpa.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import sgpa.controller.exception.ForbiddenException;
import sgpa.model.Participacao;
import sgpa.model.Projeto;
import sgpa.service.MessageService;
import sgpa.service.ProjetoService;

@Controller
public class ProjetoBaseController {

	@Autowired
	protected ProjetoService projetoService;

	@Autowired
	protected MessageService ms;

	protected Projeto projeto;
	protected Participacao participacao;

	@ModelAttribute
	public void carregarDependencias(@PathVariable Long idProjeto, Model model, Principal principal) {
		projeto = projetoService.obterPorId(idProjeto);
		participacao = projetoService.obterParticipacaoProjeto(principal.getName(), idProjeto);
		model.addAttribute("projeto", projeto);
		model.addAttribute("participacao", participacao);
	}

	protected Boolean usuarioLogadoECoordenador() {
		if (!projeto.isCoordenador(participacao.getUsuario().getEmail()))
			throw new ForbiddenException();
		return true;
	}

	protected ModelAndView redirecionarParaModulo(String nomeModulo, String mensagem,
			RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("mensagem", mensagem);
		return new ModelAndView(gerarLinkRedirect(nomeModulo));
	}

	protected String gerarLinkRedirect(String nomeModulo) {
		String link = "redirect:/projeto/" + projeto.getId() + "/";
		link += nomeModulo;
		return link;
	}

}
