package sgpa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sgpa.model.Participacao;

@Repository
public interface ParticipacaoRepository extends JpaRepository<Participacao, Long> {

	public Optional<Participacao> findByUsuarioEmailAndProjetoId(String email, Long idProjeto);

	public Iterable<Participacao> findByProjetoId(Long idProjeto);

}
