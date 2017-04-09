package sgpa.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import sgpa.model.Departamento;
import sgpa.service.DepartamentoService;
import sgpa.service.exception.DepartamentoJaCadastradoException;

@Controller
@RequestMapping("departamento")
public class DepartamentoController {

	@Autowired
	private DepartamentoService departamentoService;

	@RequestMapping("gerenciar")
	public ModelAndView gerenciar(Departamento departamento) {
		ModelAndView mv = new ModelAndView("departamento/gerenciar");
		mv.addObject("departamentos", departamentoService.listarTodos());
		return mv;
	}

	@RequestMapping(value = "cadastrar", method = RequestMethod.POST)
	public ModelAndView salvar(@Valid Departamento departamento, BindingResult result, RedirectAttributes attributes) {

		if (result.hasErrors()) {
			return gerenciar(departamento);
		}

		try {
			Departamento departamentoCadastrado = departamentoService.cadastrar(departamento);
			attributes.addFlashAttribute("mensagem", "Departamento cadastrado com sucesso: " + departamentoCadastrado);
		} catch (DepartamentoJaCadastradoException e) {
			result.rejectValue("nome", e.getMessage(), e.getMessage());
			return gerenciar(departamento);
		}

		return new ModelAndView("redirect:/departamento/gerenciar");
	}

}
