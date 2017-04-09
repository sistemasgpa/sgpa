package sgpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sgpa.model.CategoriaProjeto;

@Repository
public interface CategoriaProjetoRepository extends JpaRepository<CategoriaProjeto, Long> {

	public List<CategoriaProjeto> findAllByOrderByNomeAsc();

}
