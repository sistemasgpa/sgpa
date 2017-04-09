package sgpa.service;

import java.util.Optional;
import java.util.UUID;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sgpa.model.TokenRecuperacaoSenha;
import sgpa.model.TokenVerificacao;
import sgpa.model.Usuario;
import sgpa.repository.TokenRecuperacaoSenhaRepository;
import sgpa.repository.TokenVerificacaoRepository;
import sgpa.service.exception.TokenVerificacaoNaoEncontradoException;

@Service
public class TokenService {

	@Autowired
	private MessageService ms;

	@Autowired
	private EmailService emailService;

	@Autowired
	private TokenVerificacaoRepository tokenVerificacaoRepository;

	@Autowired
	private TokenRecuperacaoSenhaRepository tokenRecuperacaoSenhaRepository;

	public void gerarTokenVerificacao(Usuario usuario) {
		TokenVerificacao token = new TokenVerificacao(gerarValor(), usuario);
		tokenVerificacaoRepository.save(token);
		String assunto = ms.getMessage("email.firstaccess.title");
		String texto = ms.getMessage("email.firstaccess.text")
				+ gerarLink(ms.getMessage("email.firstaccess.url"), token.getToken());

		try {
			emailService.enviar(usuario.getEmail(), assunto, texto);
		} catch (MessagingException e) {
			System.out.println(e.getMessage());
		}
	}

	public TokenVerificacao buscarTokenVerificacaoPorToken(String token) {
		Optional<TokenVerificacao> optionalTokenVerificacao = tokenVerificacaoRepository.findByToken(token);

		if (!optionalTokenVerificacao.isPresent()) {
			throw new TokenVerificacaoNaoEncontradoException("Token de verificacao não encontrado");
		}

		return optionalTokenVerificacao.get();
	}

	public void deletarTokenVerificacao(TokenVerificacao token) {
		tokenVerificacaoRepository.delete(token);
	}

	public void gerarTokenRecuperacaoSenha(Usuario usuario) {
		TokenRecuperacaoSenha token = new TokenRecuperacaoSenha(gerarValor(), usuario);
		tokenRecuperacaoSenhaRepository.save(token);
		String assunto = ms.getMessage("email.passwordforgot.title");
		String texto = ms.getMessage("email.passwordforgot.text")
				+ gerarLink(ms.getMessage("email.passwordforgot.url"), token.getToken());

		try {
			emailService.enviar(usuario.getEmail(), assunto, texto);
		} catch (MessagingException e) {
			System.out.println(e.getMessage());
		}

	}

	public TokenRecuperacaoSenha buscarTokenRecuparacaoSenhaPorToken(String token) {
		Optional<TokenRecuperacaoSenha> optionalTokenRecuperacaoSenha = tokenRecuperacaoSenhaRepository
				.findByToken(token);

		if (!optionalTokenRecuperacaoSenha.isPresent()) {
			throw new TokenVerificacaoNaoEncontradoException("Token de recuperacao não encontrado");
		}

		return optionalTokenRecuperacaoSenha.get();
	}

	public void deletarTokenRecuperacaoSenha(TokenRecuperacaoSenha token) {
		tokenRecuperacaoSenhaRepository.delete(token);
	}

	private String gerarLink(String propriedade, String token) {
		return "<a href='" + gerarURL(propriedade, token) + "'>Link de Acesso</a>";
	}

	private String gerarURL(String url, String token) {
		return url + "?" + "token=" + token;
	}

	private String gerarValor() {
		return UUID.randomUUID().toString();
	}

}
