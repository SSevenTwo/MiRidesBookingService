package cars;

import exceptions.InvalidBooking;
import exceptions.InvalidBookingFee;
import exceptions.InvalidId;
import exceptions.InvalidRefreshments;
import utilities.DateTime;
import utilities.DateUtilities;

/*
 * Class:		SilverServiceCar
 * Description:	The class represents a silver service car in a ride sharing system. 
 * Author:		Ian Nguyen
 */
public class SilverServiceCar extends Car {

	private String[] refreshments;

	// Creates SS Car
	public SilverServiceCar(String regNo, String make, String model, String driverName, int passengerCapacity,
			double bookingFee, String[] refreshments) throws InvalidRefreshments, InvalidId, InvalidBookingFee {
		super(regNo, make, model,driverName,passengerCapacity);

		// Validates booking fee
		if (bookingFee < 3.0) {
			throw new InvalidBookingFee("Error - Booking fee for Silver Service Car must be at least $3.00\n");
		}
		// Validates number of Refreshments
		if (refreshments.length < 3) {
			throw new InvalidRefreshments("Error - There must be more than 2 refreshments.\n");
		}

		// Validates that no refreshments are the same
		if (this.checkForDuplicateItem(refreshments)) {
			throw new InvalidRefreshments("Error - There must not be duplicate refreshments.\n");
		}
		super.setBookingCalculation(0.4);
		super.setBookingFee(bookingFee);
		this.refreshments = refreshments;
	}

	// Booking a SS car. Ensures within 3 days.
	@Override
	public boolean book(String firstName, String lastName, DateTime required, int numPassengers) throws InvalidBooking {
		if (DateUtilities.dateIsNotMoreThanXDays(required, 3) && DateUtilities.dateIsNotInPast(required)) {
			return super.book(firstName, lastName, required, numPassengers);
		}
		return false;
	}

	// Create toString of SS car
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.carStringDetails());
		sb.append(this.printBookingFee());
		sb.append(this.printStringRefreshments());
		sb.append(this.printBookingID());

		return sb.toString();
	}

	// Get details of SS car
	@Override
	public String getDetails() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.getCarDetails() + "\n");
		sb.append(this.printRefreshments());
		sb.append(super.printBookings());
		return sb.toString();
	}

	// Returns list of refreshments
	public String printRefreshments() {
		StringBuilder sb = new StringBuilder();
		sb.append("Refreshments Available\n");
		for (int i = 0, j = 1; i < this.refreshments.length; i++, j++) {
			sb.append(String.format("%-15s %s\n", "Item " + j, refreshments[i]));
		}
		sb.append("\n");
		return sb.toString();
	}

	// Returns string of refreshments
	public String printStringRefreshments() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0, j = 1; i < this.refreshments.length; i++, j++) {
			sb.append(String.format("%s %s:", "Item " + j, refreshments[i]));
		}
		return sb.toString();
	}

	// Returns a string of the car details. Used for saving to file.
	@Override
	public String stringToSave() {
		StringBuilder sb = new StringBuilder();
		sb.append("SSCAR:");
		sb.append(this.carStringDetails());
		sb.append(this.printBookingFee());
		sb.append(this.printStringRefreshments());

		return sb.toString();
	}

	// Checks whether there are duplicate refreshments
	private boolean checkForDuplicateItem(String[] refreshments) {
		for (int i = 0; i < refreshments.length; i++) {
			for (int j = 0; j < refreshments.length; j++) {
				if (i != j && refreshments[i].equalsIgnoreCase(refreshments[j])) {
					return true;
				}
			}
		}
		return false;
	}
	/*
	 * ALGORITHM of checkForDuplicateItem method above 
	 * BEGIN 
	 * 	RECIEVE refreshments array
	 *  START LOOP
	 *  	GRAB item from refreshments array
	 *  	COMPARE item grabbed to other items in array
	 *  	IF items are the same
	 *  		RETURN TRUE
	 *  ELSE IF not at the end of the refreshments array
	 *   	GRAB next item in refreshments array
	 *   	REPEAT LOOP
	 *   ELSE
	 *   	RETURN false
	 * 
	 * TEST 
	 * RECEIVE refreshments array
	 * GRAB 1st item
	 * COMPARE 1st item to every other item
	 * GRAB 2nd item
	 * COMPARE 2nd item to every other item
	 * GRAB 3rd item
	 * COMPARE 3rd item to every other item
	 * RETURN false
	 */
	
}
