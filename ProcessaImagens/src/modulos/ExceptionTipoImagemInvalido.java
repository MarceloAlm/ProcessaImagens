package modulos;

/** Classe para exeção do tipo imagem incorreta */
public class ExceptionTipoImagemInvalido extends Exception {
	private static final long serialVersionUID = -162817055918899968L;

	public ExceptionTipoImagemInvalido() {
	}

	public ExceptionTipoImagemInvalido(String message) {
		super(message);
	}

	public ExceptionTipoImagemInvalido(Throwable cause) {
		super(cause);
	}

	public ExceptionTipoImagemInvalido(String message, Throwable cause) {
		super(message, cause);
	}

}
