package sgpa.config;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import sgpa.model.CategoriaNoticia;
import sgpa.model.CategoriaProjeto;
import sgpa.model.Departamento;
import sgpa.model.GrupoUsuario;
import sgpa.model.Usuario;
import sgpa.repository.CategoriaNoticiaRepository;
import sgpa.repository.CategoriaProjetoRepository;
import sgpa.repository.DepartamentoRepository;
import sgpa.repository.UsuarioRepository;

@Component
public class DataLoader implements ApplicationRunner {

	@Autowired
	private PasswordEncoder passwordEncoder;

	private CategoriaNoticiaRepository categoriaNoticiaRepository;
	private DepartamentoRepository departamentoRepository;
	private CategoriaProjetoRepository categoriaProjetoRepository;
	private UsuarioRepository usuarioRepository;

	@Autowired
	public DataLoader(CategoriaNoticiaRepository categoriaNoticiaRepository,
			DepartamentoRepository departamentoRepository, CategoriaProjetoRepository categoriaProjetoRepository,
			UsuarioRepository usuarioRepository) {
		this.categoriaNoticiaRepository = categoriaNoticiaRepository;
		this.departamentoRepository = departamentoRepository;
		this.categoriaProjetoRepository = categoriaProjetoRepository;
		this.usuarioRepository = usuarioRepository;
	}

	@Override
	public void run(ApplicationArguments arg0) throws Exception {

		if (categoriaNoticiaRepository.count() == 0) {
			categoriaNoticiaRepository.save(new CategoriaNoticia("Plataforma"));
			categoriaNoticiaRepository.save(new CategoriaNoticia("Editais"));
		}

		if (departamentoRepository.count() == 0) {
			departamentoRepository.save(new Departamento("Cordenação de Extensão", "CEX"));
			departamentoRepository.save(new Departamento("Cordenação de Pesquisa", "CPE"));
			departamentoRepository.save(new Departamento("Cordenação de Ensino", "CEN"));
			departamentoRepository.save(new Departamento("Cordenação de Estágio", "CES"));
		}

		if (categoriaProjetoRepository.count() == 0) {
			categoriaProjetoRepository.save(new CategoriaProjeto("Pesquisa"));
			categoriaProjetoRepository.save(new CategoriaProjeto("Extensão"));
			categoriaProjetoRepository.save(new CategoriaProjeto("Ensino"));
			categoriaProjetoRepository.save(new CategoriaProjeto("Demanda Interna"));
			categoriaProjetoRepository.save(new CategoriaProjeto("Demanda Externa"));
			categoriaProjetoRepository.save(new CategoriaProjeto("Institucional"));
		}

		if (usuarioRepository.count() == 0) {
			usuarioRepository.save(gerarAdminInicial());
		}

	}

	private Usuario gerarAdminInicial() {
		Usuario usuario = new Usuario();
		usuario.setNome("Administrador Plataforma");
		usuario.setEmail("admin@sgpa.info");
		usuario.setSenha(passwordEncoder.encode("123"));
		usuario.setGrupo(GrupoUsuario.ADMIN);
		usuario.setEmailConfirmado(true);
		usuario.setAtivo(true);
		usuario.setDepartamento(departamentoRepository.findOne(1L));
		usuario.setDataNascimento(new Date());
		usuario.setCpf("351.684.438-85");
		return usuario;
	}

}
