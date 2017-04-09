package sgpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sgpa.model.Noticia;
import sgpa.repository.helper.noticia.NoticiaRepositoryQueries;

@Repository
public interface NoticiaRepository extends JpaRepository<Noticia, Long>, NoticiaRepositoryQueries {

}
