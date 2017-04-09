package sgpa.repository.helper.projeto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import sgpa.model.Projeto;
import sgpa.repository.filter.ProjetoFilter;

public interface ProjetoRepositoryQueries {

	public Page<Projeto> filtrar(ProjetoFilter filtro, Pageable pageable);
}
