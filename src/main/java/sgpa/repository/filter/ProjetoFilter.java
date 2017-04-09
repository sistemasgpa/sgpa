package sgpa.repository.filter;

import sgpa.model.CategoriaProjeto;
import sgpa.model.StatusProjeto;

public class ProjetoFilter {

	private String titulo;
	private StatusProjeto status;
	private CategoriaProjeto categoria;

	public ProjetoFilter() {

	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public StatusProjeto getStatus() {
		return status;
	}

	public void setStatus(StatusProjeto status) {
		this.status = status;
	}

	public CategoriaProjeto getCategoria() {
		return categoria;
	}

	public void setCategoria(CategoriaProjeto categoria) {
		this.categoria = categoria;
	}

}
