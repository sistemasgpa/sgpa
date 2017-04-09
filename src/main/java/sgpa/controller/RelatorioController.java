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
import sgpa.controller.page.EnvioRelatorio;
import sgpa.model.Participacao;
import sgpa.model.Projeto;
import sgpa.model.Relatorio;
import sgpa.service.ProjetoService;
import sgpa.service.RelatorioService;

@Controller
@RequestMapping("projeto/{idProjeto}/relatorio")
public class RelatorioController {

	private ProjetoService projetoService;
	private RelatorioService relatorioService;

	private Projeto projeto;
	private Participacao participacao;

	@Autowired
	public RelatorioController(ProjetoService projetoService, RelatorioService relatorioService) {
		this.projetoService = projetoService;
		this.relatorioService = relatorioService;
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
		ModelAndView mv = new ModelAndView("relatorio/listar");
		Iterable<Relatorio> relatorios = relatorioService.listarTodosPorProjeto(projeto);
		mv.addObject("relatorios", relatorios);

		return mv;
	}

	@GetMapping("cadastrar")
	public ModelAndView cadastrar(Relatorio relatorio, BindingResult result) {
		ModelAndView mv = new ModelAndView("relatorio/cadastrar");
		mv.addObject("listaParticipacaoProjeto", projeto.getEquipe());
		return mv;
	}

	@GetMapping("{idRelatorio}/enviar")
	public ModelAndView enviar(@PathVariable Long idRelatorio, EnvioRelatorio envioRelatorio, BindingResult result) {
		Relatorio relatorioEncontrado = verificarSeRelatorioExiste(idRelatorio);
		verificarSeParticipantePossuiPermissao(relatorioEncontrado);

		ModelAndView mv = new ModelAndView("relatorio/enviar");
		mv.addObject("relatorio", relatorioEncontrado);
		return mv;
	}

	@PostMapping("{idRelatorio}/enviar")
	public ModelAndView salvarEnvio(@PathVariable Long idRelatorio, @Valid EnvioRelatorio envioRelatorio,
			BindingResult result, RedirectAttributes redirectAttributes) {
		Relatorio relatorioEncontrado = verificarSeRelatorioExiste(idRelatorio);
		verificarSeParticipantePossuiPermissao(relatorioEncontrado);

		if (result.hasErrors()) {
			return enviar(idRelatorio, envioRelatorio, result);
		}

		relatorioService.enviar(relatorioEncontrado, envioRelatorio.getTexto());

		redirectAttributes.addFlashAttribute("mensagem", "O Relatório foi enviado com sucesso.");
		return new ModelAndView("redirect:/projeto/" + projeto.getId() + "/relatorio");
	}

	@PostMapping()
	public ModelAndView salvar(@Valid Relatorio relatorio, BindingResult result,
			RedirectAttributes redirectAttributes) {

		if (result.hasErrors()) {
			return cadastrar(relatorio, result);
		}

		relatorioService.salvar(relatorio);

		redirectAttributes.addFlashAttribute("mensagem", "O Relatório foi salvo.");
		return new ModelAndView("redirect:/projeto/" + projeto.getId() + "/relatorio");

	}

	private Relatorio verificarSeRelatorioExiste(Long idRelatorio) {
		Relatorio relatorioVerificacao = relatorioService.obterPorIdPorProjetoId(idRelatorio, projeto.getId());
		if (relatorioVerificacao != null)
			return relatorioVerificacao;
		else
			throw new ResourceNotFoundException();
	}

	private void verificarSeParticipantePossuiPermissao(Relatorio relatorio) {
		if (!relatorio.getParticipacao().getId().equals(participacao.getId()))
			if (!participacao.isCoordenador())
				throw new ForbiddenException();
	}

}
