package exceptions;
/*
 * Class:			InvalidBookingFee
 * Description:		InvalidBookingFee Exception
 * Author:			Ian Nguyen - S3788210
 */
public class InvalidBookingFee extends Exception {

	public InvalidBookingFee(String message) {
		super(message);
	}

	public InvalidBookingFee() {
		super();
	}
}
