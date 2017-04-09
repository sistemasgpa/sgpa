package sgpa.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import sgpa.controller.exception.ForbiddenException;
import sgpa.controller.exception.ResourceNotFoundException;
import sgpa.model.Arquivo;
import sgpa.service.ArquivoService;

@Controller
@RequestMapping("projeto/{idProjeto}/arquivo")
public class ArquivoController extends ProjetoBaseController {

	private static final String VIEW_ARQUIVO_LISTAR = "arquivo/listar";

	private ArquivoService arquivoService;

	@Autowired
	public ArquivoController(ArquivoService arquivoService) {
		super();
		this.arquivoService = arquivoService;
	}

	@GetMapping
	public ModelAndView listar(@PathVariable Long idProjeto) {
		ModelAndView mv = new ModelAndView(VIEW_ARQUIVO_LISTAR);
		Iterable<Arquivo> arquivos = arquivoService.listarTodosPorProjeto(projeto);
		mv.addObject("arquivos", arquivos);
		return mv;
	}

	@GetMapping("{idArquivo}")
	@ResponseBody
	public void download(@PathVariable Long idProjeto, @PathVariable Long idArquivo, HttpServletResponse response)
			throws IOException {
		Arquivo arquivo = verificarSeArquivoExiste(idArquivo);
		File file = arquivoService.realizarDownload(idArquivo, idProjeto);
		InputStream in = new FileInputStream(file);
		response.setContentType(arquivo.getContentType());
		response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
		response.setHeader("Content-Length", String.valueOf(file.length()));
		FileCopyUtils.copy(in, response.getOutputStream());
	}

	@PostMapping
	public ModelAndView enviar(@PathVariable Long idProjeto, @RequestParam(name = "arquivo") MultipartFile arquivo) {
		if (arquivo != null) {
			arquivoService.salvar(arquivo, projeto, participacao);
		}
		return listar(idProjeto);
	}

	@GetMapping("{idArquivo}/excluir")
	public ModelAndView excluir(@PathVariable Long idProjeto, @PathVariable Long idArquivo) {
		Arquivo arquivo = verificarSeArquivoExiste(idArquivo);
		verificarSeParticipantePossuiPermissao(arquivo);
		arquivoService.deletar(arquivo);
		return listar(idProjeto);
	}

	private Arquivo verificarSeArquivoExiste(Long idArquivo) {
		Arquivo arquivo = arquivoService.obterPorIdPorProjetoId(idArquivo, projeto.getId());
		if (arquivo != null)
			return arquivo;
		else
			throw new ResourceNotFoundException();
	}

	private void verificarSeParticipantePossuiPermissao(Arquivo arquivo) {
		if (!arquivo.possuiPermissao(participacao.getId()))
			throw new ForbiddenException();
	}

}
