package sistema_bancario.services.exceptions;

public class DataViolationException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public DataViolationException() {
		super("Resource already exists.");
	}

	public DataViolationException(String msg) {
		super("Resource already exists. " + msg + ".");
	}
}
