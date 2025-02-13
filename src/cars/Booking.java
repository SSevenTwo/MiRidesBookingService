package cars;

import utilities.DateTime;
import utilities.DateUtilities;

/*
 * Booking Class
 * Represents a booking in a ride sharing system.
 * This class can be used by other objects not just cars.
 * Author: Ian Nguyen
 */
public class Booking {

	private String id;
	private String firstName;
	private String lastName;
	private DateTime dateBooked;
	private int numPassengers;
	private double bookingFee;
	private double kilometersTravelled;
	private double tripFee;
	private Car car;

	private final int NAME_MINIMUM_LENGTH = 3;

	public Booking(String firstName, String lastName, DateTime required, int numPassengers, Car car) {
		generateId(car.getRegistrationNumber(), firstName, lastName, required);
		validateAndSetDate(required);
		validateName(firstName, lastName);
		this.numPassengers = numPassengers;
		this.car = car;
		this.bookingFee = car.getTripFee();
	}

	/*
	 * Updates the booking record with the kilometers traveled, the booking and trip
	 * fee.
	 */
	public void completeBooking(double kilometersTravelled, double tripFee, double bookingFee) {
		this.kilometersTravelled = kilometersTravelled;
		this.tripFee = tripFee;
		this.bookingFee = bookingFee;
	}

	/*
	 * Human readable presentation of the state of the car.
	 */
	public String getDetails() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%-16s%-20s\n", " ", getRecordMarker()));
		sb.append(String.format("%-16s%-20s %s\n", " ", "id:", id));
		sb.append(String.format("%-16s%-20s $%.2f\n", " ", "Booking Fee:", bookingFee));
		if (dateBooked != null) {
			sb.append(String.format("%-16s%-20s %s\n", " ", "Pick Up Date:", dateBooked.getFormattedDate()));
		} else {
			sb.append(String.format("%-16s%-20s %s\n", " ", "Pick Up Date:", "Invalid"));
		}
		sb.append(String.format("%-16s%-20s %s\n", " ", "Name:", firstName + " " + lastName));
		sb.append(String.format("%-16s%-20s %s\n", " ", "Passengers:", numPassengers));
		if (kilometersTravelled == 0) {
			sb.append(String.format("%-16s%-20s %s\n", " ", "Travelled:", "N/A"));
			sb.append(String.format("%-16s%-20s %s\n", " ", "Trip Fee:", "N/A"));
		} else {
			sb.append(String.format("%-16s%-20s %.2f\n", " ", "Travelled:", kilometersTravelled));
			sb.append(String.format("%-16s%-20s %.2f\n", " ", "Trip Fee:", tripFee));
		}
		sb.append(String.format("%-16s%-20s %s\n", " ", "Car Id:", car.getRegistrationNumber()));

		return sb.toString();
	}

	/*
	 * Computer readable state of the car
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(id);
		sb.append(":" + bookingFee);
		if (dateBooked != null) {
			sb.append(":" + dateBooked.getEightDigitDate());
		} else {
			sb.append(":" + "Invalid");
		}
		sb.append(":" + firstName + " " + lastName);
		sb.append(":" + numPassengers);
		sb.append(":" + kilometersTravelled);
		sb.append(":" + tripFee);
		sb.append(":" + car.getRegistrationNumber());

		return sb.toString();
	}

	// Required getters
	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public DateTime getBookingDate() {
		return dateBooked;
	}

	public String getID() {
		return id;
	}

	/*
	 * A record marker mark the beginning of a record.
	 */
	private String getRecordMarker() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 40; i++) {
			sb.append("_");
		}
		sb.append("\n");
		return sb.toString();
	}

	/*
	 * Generate an id from regNo, passenger name and the date of the booking.
	 */
	private void generateId(String regNo, String firstName, String lastName, DateTime date) {
		if (firstName.length() < 3 || lastName.length() < 3 || date == null) {
			id = "Invalid";
		} else {
			id = regNo + firstName.substring(0, 3).toUpperCase() + lastName.substring(0, 3).toUpperCase()
					+ date.getEightDigitDate();
		}
	}
	
	/*ALGORITHIM of generateId method
	*BEGIN
	*	GET registration number, first and last name, date
	*	IF first name or last name is less than 3 letters
	*		return Invalid ID
	*	END IF
	*	ELSE
	*		CONCATINATE Registration number, first 3 letters of name and surname, date
	*	RETURN STRING
	*	END ELSE
	*END
	*
	*TEST
	*	GET registration number, first and last name, date
	*	CHECK whether first name or last name is less than 3. It is not
	*	RETURN Registration number, first 3 letters of name and surname, date as string
	*END
	*
	*TEST
	*	GET booking fee value of 1.5
	*	GET kilometers traveled of 30
	*	COMPUTE booking fee + 30% * 1.5 * Kilometers traveled
	*	RETURN result of 15
	*/

	/*
	 * Ensures the name is more than three characters
	 */
	private void validateName(String firstName, String lastName) {
		if (firstName.length() >= NAME_MINIMUM_LENGTH && lastName.length() >= NAME_MINIMUM_LENGTH) {
			this.firstName = firstName;
			this.lastName = lastName;
		} else {
			firstName = "Invalid";
			lastName = "Invalid";
		}
	}

	/*
	 * Ensures the date is not in the past.
	 */
	private void validateAndSetDate(DateTime date) {
		if (DateUtilities.dateIsNotInPast(date) && DateUtilities.dateIsNotMoreThanXDays(date, 7)) {
			dateBooked = date;
		} else {
			dateBooked = null;
		}
	}
}
