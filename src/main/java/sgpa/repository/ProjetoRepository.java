package sgpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sgpa.model.Projeto;
import sgpa.repository.helper.projeto.ProjetoRepositoryQueries;

@Repository
public interface ProjetoRepository extends JpaRepository<Projeto, Long>, ProjetoRepositoryQueries {

	Iterable<Projeto> findAllByEquipeUsuarioEmail(String email);

	Page<Projeto> findByVisivel(Boolean visivel, Pageable pageable);

}
