package sgpa.repository.helper.noticia;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import sgpa.model.Noticia;
import sgpa.repository.filter.NoticiaFilter;

public interface NoticiaRepositoryQueries {
	public Page<Noticia> filtrar(NoticiaFilter filtro, Pageable pageable);
}
