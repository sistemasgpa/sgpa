package sgpa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sgpa.model.Estudo;
import sgpa.model.Projeto;
import sgpa.repository.EstudoRepository;

@Service
public class EstudoService {

	private EstudoRepository estudoRepository;

	@Autowired
	public EstudoService(EstudoRepository estudoRepository) {
		this.estudoRepository = estudoRepository;
	}

	public Iterable<Estudo> listarTodos() {
		return estudoRepository.findAll();
	}

	public Iterable<Estudo> listarTodosPorProjeto(Projeto projeto) {
		return estudoRepository.findByProjeto(projeto);
	}

	public Estudo obterPorId(Long id) {
		return estudoRepository.findOne(id);
	}

	public Estudo obterPorIdPorProjetoId(Long idEstudo, Long idProjeto) {
		return estudoRepository.findByIdAndProjetoId(idEstudo, idProjeto);
	}

	public Estudo salvar(Estudo estudo) {
		return estudoRepository.save(estudo);
	}

	public void deletar(Estudo estudo) {
		estudoRepository.delete(estudo);
	}

}
