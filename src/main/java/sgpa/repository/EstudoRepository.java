package sgpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sgpa.model.Estudo;
import sgpa.model.Projeto;

@Repository
public interface EstudoRepository extends JpaRepository<Estudo, Long> {

	public Iterable<Estudo> findByProjeto(Projeto projeto);

	public Estudo findByIdAndProjetoId(Long idEstudo, Long idProjeto);

}
