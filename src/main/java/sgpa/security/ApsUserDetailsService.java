package sgpa.security;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import sgpa.model.Usuario;
import sgpa.repository.UsuarioRepository;

@Service
public class ApsUserDetailsService implements UserDetailsService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<Usuario> optionalUser = usuarioRepository.findByEmailIgnoreCase(email);
		if (!optionalUser.isPresent()) {
			throw new UsernameNotFoundException("Usuário não encontrado");
		}

		Usuario usuario = optionalUser.get();
		MyUser myUser = new MyUser(usuario.getEmail(), usuario.getSenha(), usuario.getAtivo(), true, true, true,
				getAuthorities("ROLE_" + usuario.getGrupo().toString()));
		myUser.setNome(usuario.getNome());
		return myUser;
	}

	private Collection<? extends GrantedAuthority> getAuthorities(String role) {
		return Arrays.asList(new SimpleGrantedAuthority(role));
	}

}
