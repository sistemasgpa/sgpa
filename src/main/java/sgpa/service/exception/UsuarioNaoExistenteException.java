package sgpa.service.exception;

public class UsuarioNaoExistenteException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public UsuarioNaoExistenteException(String message) {
		super(message);
	}
}
