package exceptions;

public class InvalidBooking extends Exception {

	public InvalidBooking(String message) {
		super(message);
	}
	
	public InvalidBooking() {
		super();
	}
}
