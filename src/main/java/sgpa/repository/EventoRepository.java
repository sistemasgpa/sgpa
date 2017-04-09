package sgpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sgpa.model.Evento;
import sgpa.model.Projeto;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {

	List<Evento> findByProjeto(Projeto projeto);
}
