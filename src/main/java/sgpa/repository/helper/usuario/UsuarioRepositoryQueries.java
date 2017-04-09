package sgpa.repository.helper.usuario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import sgpa.model.Usuario;
import sgpa.repository.filter.UsuarioFilter;

public interface UsuarioRepositoryQueries {

	public Page<Usuario> filtrar(UsuarioFilter filtro, Pageable pageable);

}
