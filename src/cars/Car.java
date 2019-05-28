package cars;

import exceptions.InvalidBooking;
import exceptions.InvalidDate;
import exceptions.InvalidId;
import utilities.DateTime;
import utilities.DateUtilities;
import utilities.MiRidesUtilities;

/*
 * Class:		Car
 * Description:	The class represents a car in a ride sharing system. 
 * Author:		Ian Nguyen
 */
public class Car {
	// Car attributes
	private String regNo;
	private String make;
	private String model;
	private String driverName;
	private int passengerCapacity;
	private double bookingFee;
	private double bookingCalculation;

	// Tracking bookings
	private Booking[] currentBookings;
	private Booking[] pastBookings;
	@SuppressWarnings("unused")
	private boolean available;
	private int bookingSpotAvailable = 0;
	private double tripFee = 0;

	// Constants
	private final int MAXIUM_PASSENGER_CAPACITY = 10;
	private final int MINIMUM_PASSENGER_CAPACITY = 1;

	// Method used to create standard cars
	public Car(String regNo, String make, String model, String driverName, int passengerCapacity) throws InvalidId {
		setRegNo(regNo); // Validates and sets registration number
		if (this.regNo.equalsIgnoreCase("invalid")) {
			throw new InvalidId("Error - Invalid Registration number.");
		}
		setPassengerCapacity(passengerCapacity); // Validates and sets passenger capacity

		this.make = make;
		this.model = model;
		this.driverName = driverName;
		available = true;
		currentBookings = new Booking[5];
		pastBookings = new Booking[10];
		this.bookingCalculation = 0.3;
		this.bookingFee = 1.5;
	}

	//Method for booking the car
	public boolean book(String firstName, String lastName, DateTime required, int numPassengers) throws InvalidBooking {
		boolean booked = false;
		// Does car have five bookings
		if (!bookingAvailable()) {
			throw new InvalidBooking("Error - The car is currently completely booked.");
		}

		// boolean dateAvailable = notCurrentlyBookedOnDate(required);
		if (!notCurrentlyBookedOnDate(required)) {
			throw new InvalidBooking("Error - The car is currently booked on this date.");
		}
		// Date is within range, not in past and within the next week
		if (!dateIsValid(required)) {
			throw new InvalidBooking("Error - The input date is in the past, or too far in advance.");
		}

		// Number of passengers does not exceed the passenger capacity and is not zero.
		if (!numberOfPassengersIsValid(numPassengers)) {
			throw new InvalidBooking("Error - The number of passengers is invalid.");
		}
		
		// Validates the name
		if (!(firstName.length() >= 3) || !(lastName.length() >= 3)) {
			throw new InvalidBooking("Error - The first and last name must be of at least 3 characters.");
		}
		// Booking is permissible
		tripFee = this.bookingFee;
		Booking booking = new Booking(firstName, lastName, required, numPassengers, this);
		currentBookings[bookingSpotAvailable] = booking;
		bookingSpotAvailable++;
		booked = true;
		return booked;
	}
	
	/*
	 * ALGORITHM of booking method above 
	 * BEGIN 
	 * 	RECIEVE booking information 
	 *  IF 	car is not available to book
	 *  	THROW exception
	 *  ELSE IF booked on the current date
	 *  	THROW exception
	 *  ELSE IF booking date is not valid
	 *  	THROW exception
	 *  ELSE IF number of passengers is not valid
	 *  	THROW exception
	 *  ELSE IF first or last name is less than 3
	 *  	THROW exception
	 *  ELSE
	 * 	CREATE new booking object 
	 * 	ADD booking object to next available position in Booking Array
	 * END
	 * 
	 * TEST 
	 * RECEIVE booking information 
	 * CHECK if car is available to book, not booked on current date, date is valid, if no. of passengers valid
	 * 		 and if the first and last name is less than 3. No to all.
	 * CREATE new booking object 
	 * ADD booking object to empty spot in booking array
	 * RETURN successful booking!
	 */

	/*
	 * Completes a booking based on the name of the passenger and the booking date.
	 */
	public String completeBooking(String firstName, String lastName, DateTime dateOfBooking, double kilometers) {
		// Find booking in current bookings by passenger and date
		int bookingIndex = getBookingByDate(firstName, lastName, dateOfBooking);

		if (bookingIndex == -1) {
			return "Booking not found.";
		}

		return completeBooking(bookingIndex, kilometers);
	}

	/*
	 * Completes a booking based on the name of the passenger.
	 */
	public String completeBooking(String firstName, String lastName, double kilometers) {
		int bookingIndex = getBookingByName(firstName, lastName);

		if (bookingIndex == -1) {
			return "Booking not found.";
		} else {
			return completeBooking(bookingIndex, kilometers);
		}
	}

	/*
	 * Checks the current bookings to see if any of the bookings are for the current
	 * date. ALGORITHM BEGIN CHECK All bookings IF date supplied matches date for
	 * any booking date Return true ELSE Return false END
	 * 
	 */
	public boolean isCarBookedOnDate(DateTime dateRequired) throws InvalidDate {
		boolean carIsBookedOnDate = false;
		if (!DateUtilities.dateIsNotInPast(dateRequired)) {
			throw new InvalidDate("Error - Date is in the past.");
		}
		for (int i = 0; i < currentBookings.length; i++) {
			if (currentBookings[i] != null) {
				if (DateUtilities.datesAreTheSame(dateRequired, currentBookings[i].getBookingDate())) {
					carIsBookedOnDate = true;
				}
			}
		}
		return carIsBookedOnDate;
	}

	/*
	 * Retrieves a booking id based on the name and the date of the booking
	 */
	public String getBookingID(String firstName, String lastName, DateTime dateOfBooking) {
		System.out.println();
		for (int i = 0; i < currentBookings.length; i++) {
			if (currentBookings[i] != null) {
				Booking booking = currentBookings[i];
				boolean firstNameMatch = booking.getFirstName().toUpperCase().equals(firstName.toUpperCase());
				boolean lastNameMatch = booking.getLastName().toUpperCase().equals(lastName.toUpperCase());
				int days = DateTime.diffDays(dateOfBooking, booking.getBookingDate());
				if (firstNameMatch && lastNameMatch && days == 0) {
					return booking.getID();
				}
			}
		}
		return "Booking not found";
	}

	/*
	 * Human readable presentation of the state of the car.
	 */
	public String getDetails() {
		StringBuilder sb = new StringBuilder();

		sb.append(this.getCarDetails() + "\n");
		sb.append(this.printBookings());
		return sb.toString();
	}

	/*
	 * Computer readable state of the car
	 */

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.carStringDetails());
		sb.append(this.printBookingFee());
		sb.append(this.printBookingID());
		return sb.toString();
	}

	// Required getters
	public String getRegistrationNumber() {
		return regNo;
	}

	public String getDriverName() {
		return driverName;
	}

	public double getTripFee() {
		return tripFee;
	}
	
	//Required setters
	
	protected void setBookingFee(double bookingFee) {
		this.bookingFee = bookingFee;
	}

	protected void setBookingCalculation(double bookingCalculation) {
		this.bookingCalculation = bookingCalculation;
	}

	/*
	 * Checks to see if any past bookings have been recorded
	 */
	private boolean hasBookings(Booking[] bookings) {
		boolean found = false;
		for (int i = 0; i < bookings.length; i++) {
			if (bookings[i] != null) {
				found = true;
			}
		}
		return found;
	}



	/*
	 * Processes the completion of the booking
	 */
	private String completeBooking(int bookingIndex, double kilometers) {
		tripFee = 0;
		Booking booking = currentBookings[bookingIndex];
		// Remove booking from current bookings array.
		currentBookings[bookingIndex] = null;
		bookingSpotAvailable = bookingIndex;

		// call complete booking on Booking object
		// double kilometersTravelled = Math.random()* 100;
		double fee = kilometers * (this.bookingFee * this.bookingCalculation);
		tripFee += fee;
		booking.completeBooking(kilometers, fee, this.bookingFee);
		// add booking to past bookings
		for (int i = 0; i < pastBookings.length; i++) {
			if (pastBookings[i] == null) {
				pastBookings[i] = booking;
				break;
			}
		}
		String result = String.format("Thank you for riding with MiRide.\nWe hope you enjoyed your trip.\n$"
				+ "%.2f has been deducted from your account.", tripFee);
		tripFee = 0;
		return result;
	}

	/*
	 * Gets the position in the array of a booking based on a name and date. Returns
	 * the index of the booking if found. Otherwise it returns -1 to indicate the
	 * booking was not found.
	 */
	private int getBookingByDate(String firstName, String lastName, DateTime dateOfBooking) {
		System.out.println();
		for (int i = 0; i < currentBookings.length; i++) {
			if (currentBookings[i] != null) {
				Booking booking = currentBookings[i];
				boolean firstNameMatch = booking.getFirstName().toUpperCase().equals(firstName.toUpperCase());
				boolean lastNameMatch = booking.getLastName().toUpperCase().equals(lastName.toUpperCase());
				boolean dateMatch = DateUtilities.datesAreTheSame(dateOfBooking, currentBookings[i].getBookingDate());
				if (firstNameMatch && lastNameMatch && dateMatch) {
					return i;
				}
			}
		}
		return -1;
	}

	/*
	 * Gets the position in the array of a booking based on a name. Returns the
	 * index of the booking if found. Otherwise it returns -1 to indicate the
	 * booking was not found.
	 */
	public int getBookingByName(String firstName, String lastName) {
		for (int i = 0; i < currentBookings.length; i++) {
			if (currentBookings[i] != null) {
				boolean firstNameMatch = currentBookings[i].getFirstName().toUpperCase()
						.equals(firstName.toUpperCase());
				boolean lastNameMatch = currentBookings[i].getLastName().toUpperCase().equals(lastName.toUpperCase());
				if (firstNameMatch && lastNameMatch) {
					return i;
				}
			}
		}
		return -1;
	}

	/*
	 * A record marker mark the beginning of a record.
	 */
	private String getRecordMarker() {
		final int RECORD_MARKER_WIDTH = 60;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < RECORD_MARKER_WIDTH; i++) {
			sb.append("_");
		}
		sb.append("\n");
		return sb.toString();
	}

	/*
	 * Checks to see if the number of passengers falls within the accepted range.
	 */
	private boolean numberOfPassengersIsValid(int numPassengers) {
		if (numPassengers >= MINIMUM_PASSENGER_CAPACITY && numPassengers < MAXIUM_PASSENGER_CAPACITY
				&& numPassengers <= passengerCapacity) {
			return true;
		}
		return false;
	}

	/*
	 * Checks that the date is not in the past or more than 7 days in the future.
	 */
	private boolean dateIsValid(DateTime date) {
		return DateUtilities.dateIsNotInPast(date) && DateUtilities.dateIsNotMoreThanXDays(date, 7);
	}

	/*
	 * Indicates if a booking spot is available. If it is then the index of the
	 * available spot is assigned to bookingSpotFree.
	 */
	private boolean bookingAvailable() {
		for (int i = 0; i < currentBookings.length; i++) {
			if (currentBookings[i] == null) {
				if (i == currentBookings.length - 1) {
					available = false;
				} else {
					available = true;
				}
				bookingSpotAvailable = i;
				return true;
			}
		}
		return false;
	}

	/*
	 * Checks to see if if the car is currently booked on the date specified.
	 */
	private boolean notCurrentlyBookedOnDate(DateTime date) {
		boolean foundDate = true;
		for (int i = 0; i < currentBookings.length; i++) {
			if (currentBookings[i] != null) {
				int days = DateTime.diffDays(date, currentBookings[i].getBookingDate());
				if (days == 0) {
					return false;
				}
			}
		}
		return foundDate;
	}

	/*
	 * Validates and sets the registration number
	 */
	private void setRegNo(String regNo) {
		if (!MiRidesUtilities.isRegNoValid(regNo).contains("Error:")) {
			this.regNo = regNo;
		} else {
			this.regNo = "Invalid";
		}
	}

	/*
	 * Validates and sets the passenger capacity
	 */
	private void setPassengerCapacity(int passengerCapacity) {
		boolean validPasengerCapcity = passengerCapacity >= MINIMUM_PASSENGER_CAPACITY
				&& passengerCapacity < MAXIUM_PASSENGER_CAPACITY;

		if (validPasengerCapcity) {
			this.passengerCapacity = passengerCapacity;
		} else {
			this.passengerCapacity = -1;
		}
	}

	// Returns the current and past bookings as a list.
	public String printBookings() {
		StringBuilder currentBookings = new StringBuilder();
		if (this.hasBookings(this.currentBookings)) {
			currentBookings.append("CURRENT BOOKINGS\n");
			for (Booking book : this.currentBookings) {
				if (book != null) {
					currentBookings.append(book.getDetails());
				}
			}
		}

		StringBuilder pastBookings = new StringBuilder();
		if (this.hasBookings(this.pastBookings)) {
			currentBookings.append("PAST BOOKINGS\n");
			for (Booking pBook : this.pastBookings) {
				if (pBook != null) {
					pastBookings.append(pBook.getDetails());
				}
			}
		}

		return currentBookings.toString() + pastBookings.toString();
	}

	// Returns the cars details as a string.
	public String carStringDetails() {
		StringBuilder sb = new StringBuilder();
		sb.append(regNo + ":" + make + ":" + model);
		if (driverName != null) {
			sb.append(":" + driverName);
		}
		sb.append(":" + passengerCapacity);
		if (bookingAvailable()) {
			sb.append(":" + "YES");
		} else {
			sb.append(":" + "NO");
		}
		return sb.toString();
	}

	// Returns the booking ID's as a string.
	public String printBookingID() {
		StringBuilder sb = new StringBuilder();
		if (this.hasBookings(this.currentBookings)) {
			for (Booking book : this.currentBookings) {
				if (book != null) {
					sb.append("|" + book.toString());
				}
			}
		}
		if (this.hasBookings(this.pastBookings)) {
			for (Booking pBook : this.pastBookings) {
				if (pBook != null) {
					sb.append("|" + pBook.toString());
				}
			}
		}
		return sb.toString();
	}

	// Returns the booking fee as a string.
	public String printBookingFee() {
		return ":" + this.bookingFee + ":";
	}

	// Returns a list of all the car details.
	public String getCarDetails() {
		StringBuilder sb = new StringBuilder();

		sb.append(getRecordMarker());
		sb.append(String.format("%-15s %s\n", "Reg No:", regNo));
		sb.append(String.format("%-15s %s\n", "Make & Model:", make + " " + model));

		sb.append(String.format("%-15s %s\n", "Driver Name:", driverName));
		sb.append(String.format("%-15s %s\n", "Capacity:", passengerCapacity));
		sb.append(String.format("%-15s %s\n", "Standard Fee:", bookingFee));

		if (bookingAvailable()) {
			sb.append(String.format("%-15s %s\n", "Available:", "YES"));
		} else {
			sb.append(String.format("%-15s %s\n", "Available:", "NO"));
		}
		return sb.toString();
	}

	// Returns a string of the car details. Used for saving to file.
	public String stringToSave() {
		StringBuilder sb = new StringBuilder();
		sb.append("SDCAR:");
		sb.append(this.carStringDetails());
		sb.append(this.printBookingFee());
		return sb.toString();
	}

}
