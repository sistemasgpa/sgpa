package sgpa.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Projeto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Título é obrigatório")
	private String titulo;

	@NotBlank(message = "Descrição é obrigatório")
	@Lob
	private String descricao;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	private Date dataInicio;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	private Date dataTermino;

	@NotNull
	private Boolean visivel;

	@NotNull(message = "Selecione uma categoria")
	@ManyToOne
	@JoinColumn(name = "categoria_id")
	private CategoriaProjeto categoria;

	@NotNull(message = "Selecione um Status")
	@Enumerated(EnumType.STRING)
	private StatusProjeto status;

	@OneToMany(mappedBy = "projeto", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Participacao> equipe;

	@OneToMany(mappedBy = "projeto", cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("dataInicio ASC")
	private List<Milestone> milestones;

	@OneToMany(mappedBy = "projeto", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Tarefa> tarefas;

	@OneToMany(mappedBy = "projeto", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Estudo> estudos;

	@OneToMany(mappedBy = "projeto", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Relatorio> relatorios;

	@OneToMany(mappedBy = "projeto", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Evento> eventos;

	@OneToMany(mappedBy = "projeto", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Arquivo> arquivos;

	public Projeto() {
		if (equipe == null)
			equipe = new ArrayList<>();
		if (milestones == null)
			milestones = new ArrayList<>();
		if (tarefas == null)
			tarefas = new ArrayList<>();
		if (estudos == null)
			estudos = new ArrayList<>();
		if (relatorios == null)
			relatorios = new ArrayList<>();
		if (eventos == null)
			eventos = new ArrayList<>();
		if (arquivos == null)
			arquivos = new ArrayList<>();

	}

	public Projeto(String titulo, String descricao) {
		this.titulo = titulo;
		this.descricao = descricao;
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

	public Date getDataTermino() {
		return dataTermino;
	}

	public void setDataTermino(Date dataTermino) {
		this.dataTermino = dataTermino;
	}

	public CategoriaProjeto getCategoria() {
		return categoria;
	}

	public void setCategoria(CategoriaProjeto categoria) {
		this.categoria = categoria;
	}

	public StatusProjeto getStatus() {
		return status;
	}

	public void setStatus(StatusProjeto status) {
		this.status = status;
	}

	public Boolean getVisivel() {
		return visivel;
	}

	public void setVisivel(Boolean visivel) {
		this.visivel = visivel;
	}

	public List<Participacao> getEquipe() {
		return equipe;
	}

	public void setEquipe(List<Participacao> equipe) {
		this.equipe = equipe;
	}

	public Boolean possuiCoordenador() {
		for (Participacao participacao : equipe) {
			if (participacao.getTipoParticipacao().equals(TipoParticipacao.COORDENADOR)) {
				return true;
			}
		}
		return false;
	}

	public List<Milestone> getMilestones() {
		return milestones;
	}

	public void setMilestones(List<Milestone> milestones) {
		this.milestones = milestones;
	}

	public List<Tarefa> getTarefas() {
		return tarefas;
	}

	public void setTarefas(List<Tarefa> tarefas) {
		this.tarefas = tarefas;
	}

	public List<Estudo> getEstudos() {
		return estudos;
	}

	public void setEstudos(List<Estudo> estudos) {
		this.estudos = estudos;
	}

	public List<Relatorio> getRelatorios() {
		return relatorios;
	}

	public void setRelatorios(List<Relatorio> relatorios) {
		this.relatorios = relatorios;
	}

	public List<Evento> getEventos() {
		return eventos;
	}

	public void setEventos(List<Evento> eventos) {
		this.eventos = eventos;
	}

	public List<Arquivo> getArquivos() {
		return arquivos;
	}

	public void setArquivos(List<Arquivo> arquivos) {
		this.arquivos = arquivos;
	}

	public Boolean isCoordenador(String email) {
		for (Participacao participacao : this.getEquipe()) {
			if (participacao.getUsuario().getEmail().equals(email)
					&& participacao.getTipoParticipacao().equals(TipoParticipacao.COORDENADOR))
				return true;
		}
		return false;
	}

	public Participacao obterParticipacaoPorEmail(String email) {
		for (Participacao participacao : this.getEquipe()) {
			if (participacao.getUsuario().getEmail().equals(email))
				return participacao;
		}
		return null;
	}

	public List<Usuario> obterCoordenadores() {
		ArrayList<Usuario> coordenadores = new ArrayList<>();
		for (Participacao participacao : this.getEquipe()) {
			if (participacao.getTipoParticipacao().equals(TipoParticipacao.COORDENADOR))
				coordenadores.add(participacao.getUsuario());
		}
		return coordenadores;
	}

	public List<Usuario> obterMembros() {
		ArrayList<Usuario> membros = new ArrayList<>();
		for (Participacao participacao : this.getEquipe()) {
			if (!participacao.getTipoParticipacao().equals(TipoParticipacao.COORDENADOR))
				membros.add(participacao.getUsuario());
		}
		return membros;
	}

}
