package sgpa.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sgpa.model.Departamento;
import sgpa.repository.DepartamentoRepository;
import sgpa.service.exception.DepartamentoJaCadastradoException;

@Service
public class DepartamentoService {

	@Autowired
	private DepartamentoRepository departamentoRepository;

	public Iterable<Departamento> listarTodos() {
		return departamentoRepository.findAll();
	}

	public Departamento obterPorId(Long id) {
		return departamentoRepository.findOne(id);
	}

	public Departamento salvar(Departamento departamento) {
		return departamentoRepository.save(departamento);
	}

	public void deletar(Long id) {
		departamentoRepository.delete(id);
	}

	public Departamento cadastrar(Departamento departamento) {
		Optional<Departamento> departamentoOptional = departamentoRepository
				.findByNomeIgnoreCase(departamento.getNome());
		if (departamentoOptional.isPresent()) {
			throw new DepartamentoJaCadastradoException("Departamento já cadastrado");
		}

		return salvar(departamento);
	}

}
