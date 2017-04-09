package sgpa.security;

import org.hibernate.validator.constraints.NotBlank;

public class PrimeiroAcesso {

	@NotBlank(message = "Senha é Obrigatório")
	private String senha;

	@NotBlank(message = "Confirmação Senha é Obrigatório")
	private String confirmacaoSenha;

	@NotBlank(message = "Token não pode ser vazio")
	private String token;

	public PrimeiroAcesso() {
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getConfirmacaoSenha() {
		return confirmacaoSenha;
	}

	public void setConfirmacaoSenha(String confirmacaoSenha) {
		this.confirmacaoSenha = confirmacaoSenha;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Boolean eIgual() {
		return senha.equals(confirmacaoSenha);
	}

}
