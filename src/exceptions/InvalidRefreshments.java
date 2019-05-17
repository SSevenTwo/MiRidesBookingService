package exceptions;

public class InvalidRefreshments extends Exception {
	
	public InvalidRefreshments(String message) {
		super(message);
	}
	
	public InvalidRefreshments() {
		super();
	}
}
