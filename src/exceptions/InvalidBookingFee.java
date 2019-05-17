package exceptions;

public class InvalidBookingFee extends Exception {

	public InvalidBookingFee(String message) {
		super(message);
	}

	public InvalidBookingFee() {
		super();
	}
}
