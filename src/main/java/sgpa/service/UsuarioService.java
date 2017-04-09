package sgpa.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import sgpa.model.Participacao;
import sgpa.model.TokenRecuperacaoSenha;
import sgpa.model.TokenVerificacao;
import sgpa.model.Usuario;
import sgpa.repository.ParticipacaoRepository;
import sgpa.repository.UsuarioRepository;
import sgpa.repository.filter.UsuarioFilter;
import sgpa.security.PrimeiroAcesso;
import sgpa.service.exception.UsuarioJaCadastradoException;
import sgpa.service.exception.UsuarioNaoExistenteException;

@Service
public class UsuarioService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private ParticipacaoRepository participacaoRepository;

	@Autowired
	private TokenService tokenService;

	public Iterable<Usuario> listarTodos() {
		return usuarioRepository.findAll();
	}

	public Iterable<Usuario> listarTodos(Pageable pageable) {
		return usuarioRepository.findAll(pageable);
	}

	public Page<Usuario> filtrar(UsuarioFilter filtro, Pageable pageable) {
		return usuarioRepository.filtrar(filtro, pageable);
	}

	public Usuario obterPorId(Long id) {
		return usuarioRepository.findOne(id);
	}

	public Usuario obterPorEmail(String email) {
		Optional<Usuario> usuarioOptional = usuarioRepository.findByEmailIgnoreCase(email);
		if (!usuarioOptional.isPresent()) {
			throw new UsuarioNaoExistenteException("Usuário já cadastrado");
		}
		return usuarioOptional.get();
	}

	public List<Usuario> listarTodosPorNome(String nome) {
		Optional<List<Usuario>> usuariosOptional = usuarioRepository.findByNomeIgnoreCaseContaining(nome);
		return usuariosOptional.orElse(new ArrayList<Usuario>());
	}

	public Usuario salvar(Usuario usuario) {
		return usuarioRepository.save(usuario);
	}

	public void deletar(Long id) {
		usuarioRepository.delete(id);
	}

	public Usuario cadastrar(Usuario usuario) {
		Optional<Usuario> usuarioOptional = usuarioRepository.findByEmailIgnoreCase(usuario.getEmail());
		if (usuarioOptional.isPresent()) {
			throw new UsuarioJaCadastradoException("Usuário já cadastrado");
		}
		usuario.setAtivo(false);
		usuario.setEmailConfirmado(false);
		usuario.setSenha("");
		Usuario usuarioSalvo = usuarioRepository.save(usuario);
		tokenService.gerarTokenVerificacao(usuarioSalvo);
		return usuarioSalvo;
	}

	public Usuario editar(Usuario usuario) {
		Usuario usuarioAntigo = this.obterPorId(usuario.getId());
		usuario.setAtivo(usuarioAntigo.getAtivo());
		usuario.setEmailConfirmado(usuarioAntigo.getEmailConfirmado());
		usuario.setSenha(usuarioAntigo.getSenha());
		return this.salvar(usuario);
	}

	public void definirSenhaPrimeiroAcesso(TokenVerificacao token, PrimeiroAcesso primeiroAcesso) {
		Usuario usuario = token.getUsuario();
		usuario.setEmailConfirmado(true);
		usuario.setAtivo(true);
		usuario.setSenha(passwordEncoder.encode(primeiroAcesso.getSenha()));
		salvar(usuario);
		tokenService.deletarTokenVerificacao(token);
	}

	public void redefinirSenha(TokenRecuperacaoSenha token, PrimeiroAcesso primeiroAcesso) {
		Usuario usuario = token.getUsuario();
		usuario.setSenha(passwordEncoder.encode(primeiroAcesso.getSenha()));
		salvar(usuario);
		tokenService.deletarTokenRecuperacaoSenha(token);
	}

	public Optional<Participacao> obterParticipacaoProjeto(String emailUsuario, Long idProjeto) {
		return participacaoRepository.findByUsuarioEmailAndProjetoId(emailUsuario, idProjeto);
	}

}
