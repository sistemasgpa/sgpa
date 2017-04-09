package sgpa.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Participacao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataEntrada;

	@ManyToOne
	@JoinColumn(name = "usuario_id")
	private Usuario usuario;

	@ManyToOne()
	@JoinColumn(name = "projeto_id")
	private Projeto projeto;

	@Enumerated(EnumType.STRING)
	private TipoParticipacao tipoParticipacao;

	private Boolean ativo;

	private Boolean emailEnviado;

	@ManyToMany(mappedBy = "responsaveis")
	private List<Tarefa> tarefas;

	@OneToMany(mappedBy = "participacao", orphanRemoval = true)
	private List<Estudo> estudos;

	@OneToMany(mappedBy = "participacao")
	private List<Relatorio> relatorios;

	@OneToMany(mappedBy = "participacao")
	private List<Arquivo> arquivos;

	public Participacao() {
		if (dataEntrada == null)
			dataEntrada = new Date();
		if (ativo == null)
			ativo = new Boolean(true);
		if (emailEnviado == null)
			emailEnviado = new Boolean(false);
		if (tarefas == null)
			tarefas = new ArrayList<>();
		if (estudos == null)
			estudos = new ArrayList<>();
		if (relatorios == null)
			relatorios = new ArrayList<>();
		if (arquivos == null)
			arquivos = new ArrayList<>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDataEntrada() {
		return dataEntrada;
	}

	public void setDataEntrada(Date dataEntrada) {
		this.dataEntrada = dataEntrada;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Projeto getProjeto() {
		return projeto;
	}

	public void setProjeto(Projeto projeto) {
		this.projeto = projeto;
	}

	public TipoParticipacao getTipoParticipacao() {
		return tipoParticipacao;
	}

	public void setTipoParticipacao(TipoParticipacao tipoParticipacao) {
		this.tipoParticipacao = tipoParticipacao;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public Boolean getEmailEnviado() {
		return emailEnviado;
	}

	public void setEmailEnviado(Boolean emailEnviado) {
		this.emailEnviado = emailEnviado;
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

	public boolean isCoordenador() {
		return tipoParticipacao.equals(TipoParticipacao.COORDENADOR) ? true : false;
	}

}
