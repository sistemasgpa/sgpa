package sgpa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sgpa.model.TokenVerificacao;

@Repository
public interface TokenVerificacaoRepository extends JpaRepository<TokenVerificacao, Long> {

	Optional<TokenVerificacao> findByToken(String token);

}
