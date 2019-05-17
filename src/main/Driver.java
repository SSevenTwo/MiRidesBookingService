package main;

import app.Menu;
import exceptions.InvalidBooking;
import exceptions.InvalidBookingFee;
import exceptions.InvalidDate;
import exceptions.InvalidId;
import exceptions.InvalidRefreshments;

public class Driver {

	public static void main(String[] args)
			throws InvalidBooking, InvalidRefreshments, InvalidId, InvalidDate, InvalidBookingFee 
	{
		Menu menu = new Menu();
		menu.run();
	}

}
