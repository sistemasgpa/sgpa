package sgpa.model;

public enum TipoParticipacao {

	COORDENADOR("Coordenador"), MEMBRO("Membro"), INTERESSADO("Interessado");

	private String descricao;

	TipoParticipacao(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

}
