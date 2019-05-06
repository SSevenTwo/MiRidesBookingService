package cars;

import utilities.DateTime;
import utilities.DateUtilities;
import utilities.MiRidesUtilities;

public class SilverServiceCar extends Car {
	
	private double bookingFee;
	private String[] refreshments;

	public SilverServiceCar(String regNo, String make, String model, String driverName, int passengerCapacity,
			double bookingFee, String[] refreshments) {
		super(regNo, make, model, driverName, passengerCapacity);
		this.bookingFee = bookingFee;
		this.refreshments = refreshments;		
	}
	
	@Override
	public boolean book(String firstName, String lastName, DateTime required, int numPassengers) {
		if(DateUtilities.dateIsNotMoreThanXDays(required, 3) && DateUtilities.dateIsNotInPast(required)) {
			return super.book(firstName, lastName, required, numPassengers);
		}
		return false;
	}
	
	@Override
	public String getDetails() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.getDetails());
		return sb.toString();
		
	}
	
	@Override
	public String printExtras() {
		StringBuilder sb = new StringBuilder();
		sb.append("Refreshments Available\n");
		sb.append(String.format("%-15s %s\n", "Item 1", "Mints"));
		sb.append(String.format("%-15s %s\n", "Item 2", "Orange Juice"));
		sb.append(String.format("%-15s %s\n\n", "Item 3", "Chocolate Bar"));
		return sb.toString();
	}
	
	@Override
	public String printBookingFee() {
		return ":"+this.bookingFee+":"+"Item 1 Mints:Item 2 Orange Juice: Item 3 Chocolate Bar";
	}
	
	
}
