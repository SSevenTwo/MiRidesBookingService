package exceptions;

public class InvalidBooking extends RuntimeException {

	public InvalidBooking(String message) {
		super(message);
	}
	
	public InvalidBooking() {
		super();
	}
}
