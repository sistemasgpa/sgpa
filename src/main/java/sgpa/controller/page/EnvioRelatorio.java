package sgpa.controller.page;

import org.hibernate.validator.constraints.NotBlank;

public class EnvioRelatorio {

	@NotBlank(message = "Texto não pode ser vazio")
	private String texto;

	public EnvioRelatorio() {
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

}
