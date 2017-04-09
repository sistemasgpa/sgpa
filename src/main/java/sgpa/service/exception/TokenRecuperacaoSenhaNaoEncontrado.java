package sgpa.service.exception;

public class TokenRecuperacaoSenhaNaoEncontrado extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public TokenRecuperacaoSenhaNaoEncontrado(String message) {
		super(message);
	}

}