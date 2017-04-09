package sgpa.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import sgpa.model.Participacao;
import sgpa.model.Projeto;
import sgpa.repository.ParticipacaoRepository;
import sgpa.repository.ProjetoRepository;
import sgpa.repository.filter.ProjetoFilter;
import sgpa.service.exception.ParticipanteNaoEMembroDoProjetoException;
import sgpa.service.exception.ProjetoNaoPossuiCoordenadorException;
import sgpa.service.exception.ProjetoNaoPossuiParticipantesException;

@Service
public class ProjetoService {

	private ProjetoRepository projectRepository;
	private ParticipacaoRepository participacaoRepository;
	private UsuarioService usuarioService;

	@Autowired
	public ProjetoService(ProjetoRepository projectRepository, ParticipacaoRepository participacaoRepository,
			UsuarioService usuarioService) {
		this.projectRepository = projectRepository;
		this.participacaoRepository = participacaoRepository;
		this.usuarioService = usuarioService;

	}

	public Iterable<Projeto> listarTodos() {
		return projectRepository.findAll();
	}

	public Page<Projeto> listarTodos(Pageable pageable) {
		return projectRepository.findAll(pageable);
	}

	public Page<Projeto> listarTodosVisiveis(Pageable pageable) {
		return projectRepository.findByVisivel(new Boolean(true), pageable);
	}

	public Iterable<Projeto> listarTodosPorUsuario(String email) {
		return projectRepository.findAllByEquipeUsuarioEmail(email);
	}

	public Page<Projeto> filtrar(ProjetoFilter filtro, Pageable pageable) {
		return projectRepository.filtrar(filtro, pageable);
	}

	public Projeto obterPorId(Long id) {
		return projectRepository.findOne(id);
	}

	public Projeto salvar(Projeto projeto) {

		if (projeto.getEquipe() == null || projeto.getEquipe().isEmpty()) {
			throw new ProjetoNaoPossuiParticipantesException("Projeto deve possuir participantes");
		}

		if (!projeto.possuiCoordenador()) {
			throw new ProjetoNaoPossuiCoordenadorException("O projeto deve possuir pelo menos um coordenador");
		}

		for (Participacao participacao : projeto.getEquipe()) {
			participacao.setProjeto(projeto);

			if (!participacao.getEmailEnviado()) {
				
			}

		}
		return projectRepository.save(projeto);
	}

	public void deletar(Long id) {
		projectRepository.delete(id);
	}

	public Iterable<Participacao> obterParticipacoesPorProjeto(Projeto projeto) {
		return participacaoRepository.findByProjetoId(projeto.getId());
	}

	public Participacao obterParticipacaoProjeto(String emailUsuario, Long idProjeto) {
		Optional<Participacao> participacaoOptional = usuarioService.obterParticipacaoProjeto(emailUsuario, idProjeto);
		if (participacaoOptional.isPresent())
			return participacaoOptional.get();
		else
			throw new ParticipanteNaoEMembroDoProjetoException("");
	}

}
