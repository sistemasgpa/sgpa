package sgpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sgpa.model.CategoriaNoticia;

@Repository
public interface CategoriaNoticiaRepository extends JpaRepository<CategoriaNoticia, Long> {

}
