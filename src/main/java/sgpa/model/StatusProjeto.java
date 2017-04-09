package sgpa.model;

public enum StatusProjeto {

	ATIVO("Ativo"), PAUSADO("Pausado"), FINALIZADO("Finalizado"), CANCELADO("Cancelado");

	private String descricao;

	StatusProjeto(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

}
