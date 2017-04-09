package sgpa.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Milestone {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Título é obrigatório")
	private String titulo;

	@Lob
	private String descricao;

	@NotNull(message = "Data início não pode ser nula")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	private Date dataInicio;

	@NotNull(message = "Data Limite não pode ser nula")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	private Date dataLimite;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	private Date dataConclusao;

	private Integer totalConcluido;

	private Boolean concluido;

	@ManyToOne
	@JoinColumn(name = "projeto_id")
	private Projeto projeto;

	@OneToMany(mappedBy = "milestone", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<Tarefa> tarefas;

	public Milestone() {
		if (totalConcluido == null)
			totalConcluido = 0;
		if (dataInicio == null)
			dataInicio = new Date();
		if (tarefas == null)
			tarefas = new ArrayList<>();
		if (concluido == null)
			concluido = false;
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

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataLimite() {
		return dataLimite;
	}

	public void setDataLimite(Date dataLimite) {
		this.dataLimite = dataLimite;
	}

	public Date getDataConclusao() {
		return dataConclusao;
	}

	public void setDataConclusao(Date dataConclusao) {
		this.dataConclusao = dataConclusao;
	}

	public Integer getTotalConcluido() {
		return totalConcluido;
	}

	public void setTotalConcluido(Integer totalConcluido) {
		this.totalConcluido = totalConcluido;
	}

	public Projeto getProjeto() {
		return projeto;
	}

	public void setProjeto(Projeto projeto) {
		this.projeto = projeto;
	}

	public List<Tarefa> getTarefas() {
		return tarefas;
	}

	public void setTarefas(List<Tarefa> tarefas) {
		this.tarefas = tarefas;
	}

	public Boolean getConcluido() {
		return concluido;
	}

	public void setConcluido(Boolean concluido) {
		this.concluido = concluido;
	}

	public String getSituacao() {
		Date hoje = new Date();
		String situacao = "";

		if (concluido || totalConcluido == 100)
			return "Concluido";

		if (totalConcluido > 0 && totalConcluido < 100)
			situacao = "Em Andamento";
		else
			situacao = "Não Iniciado";

		if (dataLimite.after(hoje))
			situacao += " (No Prazo)";
		else
			situacao += " (Atrasado)";

		return situacao;
	}

}
