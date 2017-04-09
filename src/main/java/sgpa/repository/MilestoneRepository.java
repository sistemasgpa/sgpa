package sgpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sgpa.model.Milestone;
import sgpa.model.Projeto;

@Repository
public interface MilestoneRepository extends JpaRepository<Milestone, Long> {

	public Iterable<Milestone> findByProjetoOrderByDataInicioAsc(Projeto projeto);

	public Milestone findByIdAndProjetoId(Long idMilestone, Long idProjeto);

}
