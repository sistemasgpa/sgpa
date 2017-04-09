package sgpa.repository.filter;

import sgpa.model.CategoriaNoticia;

public class NoticiaFilter {

	private String titulo;
	private CategoriaNoticia categoria;

	public NoticiaFilter() {

	}

	public NoticiaFilter(String titulo, CategoriaNoticia categoria) {
		this.titulo = titulo;
		this.categoria = categoria;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public CategoriaNoticia getCategoria() {
		return categoria;
	}

	public void setCategoria(CategoriaNoticia categoria) {
		this.categoria = categoria;
	}
}
