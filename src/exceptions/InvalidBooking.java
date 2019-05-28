package exceptions;
/*
 * Class:			InvalidBooking
 * Description:		InvalidBooking Exception
 * Author:			Ian Nguyen - S3788210
 */
public class InvalidBooking extends Exception {

	public InvalidBooking(String message) {
		super(message);
	}
	
	public InvalidBooking() {
		super();
	}
}
