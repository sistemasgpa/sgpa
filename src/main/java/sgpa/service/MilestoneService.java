package sgpa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sgpa.model.Milestone;
import sgpa.model.Projeto;
import sgpa.repository.MilestoneRepository;
import sgpa.service.exception.DataLimiteMenorQueDataInicioException;

@Service
public class MilestoneService {

	private MilestoneRepository milestoneRepository;

	@Autowired
	public MilestoneService(MilestoneRepository milestoneRepository) {
		this.milestoneRepository = milestoneRepository;
	}

	public Iterable<Milestone> listarTodos() {
		return milestoneRepository.findAll();
	}

	public Iterable<Milestone> listarTodosPorProjeto(Projeto projeto) {
		return milestoneRepository.findByProjetoOrderByDataInicioAsc(projeto);
	}

	public Milestone obterPorId(Long id) {
		return milestoneRepository.findOne(id);
	}

	public Milestone obterPorIdPorProjetoId(Long idMilestone, Long idProjeto) {
		return milestoneRepository.findByIdAndProjetoId(idMilestone, idProjeto);
	}

	public Milestone salvar(Milestone milestone) {
		if (milestone.getDataLimite().before(milestone.getDataInicio())) {
			throw new DataLimiteMenorQueDataInicioException("A data limite não pode ser menor que a data início.");
		}
		return milestoneRepository.save(milestone);
	}

	public void deletar(Long id) {
		milestoneRepository.delete(id);
	}

}
