package sgpa.model;

public enum GrupoUsuario {
	PADRAO("Padrão"), ADMIN("Administradores");

	private String descricao;

	private GrupoUsuario(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}
