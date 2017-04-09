package sgpa.model;

public enum TipoPublicacao {
	ARTIGO("Artigo"), CAPITULO_LIVRO("Capítulo de Livro"), DISSERTACAO("Dissertação"), GUIA("Guia"), INTERNET(
			"Internet"), LEGISLACAO("Legislação"), LIVRO("Livro"), MANUAL("Manual"), MONOGRAFIA(
					"Monografia"), OUTRO("Outro"), RESUMO("Resumo"), TESTE("Teste"), VIDEO("Vídeo");

	private String descricao;

	TipoPublicacao(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}
