package sgpa.controller.page;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

public class FormularioContato {

	@NotBlank(message = "Nome não pode ser vazio")
	private String nome;

	@NotBlank(message = "Email não pode ser vazio")
	@Email(message = "Email inválido")
	private String email;

	@NotBlank(message = "Mensagem não pode ser vazio")
	private String mensagem;

	public FormularioContato() {
	}

	public String getTexto() {
		String texto = "<p>Contato enviado por: ";
		texto += "<strong>" + this.getNome() + "</strong>";
		texto += " Email: <strong>" + this.getEmail() + "</strong></p>";
		texto += "<p><strong>Mensagem</strong></p>";
		texto += "<p>" + this.getMensagem() + "</p>";
		return texto;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

}
