package sgpa.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Noticia {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Título não pode ser vazio")
	private String titulo;

	@NotBlank(message = "Conteudo não pode ser vazio")
	@Lob
	private String conteudo;

	@NotNull(message = "Selecione uma data")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	private Date data;

	@ManyToOne
	@JoinColumn(name = "categoria_id")
	private CategoriaNoticia categoria;

	public Noticia() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getConteudo() {
		return conteudo;
	}

	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public CategoriaNoticia getCategoria() {
		return categoria;
	}

	public void setCategoria(CategoriaNoticia categoria) {
		this.categoria = categoria;
	}

	public String getChamada() {
		if (this.conteudo.length() > 150)
			return this.conteudo.substring(0, 150) + " ...";
		return conteudo;
	}

}
