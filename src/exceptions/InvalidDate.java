package exceptions;

public class InvalidDate extends Exception {
	
	public InvalidDate(String message) {
		super(message);
	}

	public InvalidDate() {
		super();
	}
}
