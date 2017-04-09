package sgpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sgpa.model.Projeto;
import sgpa.model.Tarefa;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {

	public Iterable<Tarefa> findByProjeto(Projeto projeto);

	public Tarefa findByIdAndProjetoId(Long idTarefa, Long idProjeto);
}
