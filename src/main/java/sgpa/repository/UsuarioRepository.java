package sgpa.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sgpa.model.Usuario;
import sgpa.repository.helper.usuario.UsuarioRepositoryQueries;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>, UsuarioRepositoryQueries {

	public Optional<Usuario> findByEmailIgnoreCase(String email);

	public Optional<List<Usuario>> findByNomeIgnoreCaseContaining(String nome);

}
