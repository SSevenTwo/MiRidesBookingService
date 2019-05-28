package main;

import app.Menu;
import exceptions.InvalidBooking;
import exceptions.InvalidBookingFee;
import exceptions.InvalidDate;
import exceptions.InvalidId;
import exceptions.InvalidRefreshments;

/*
 * Class:			Driver
 * Description:		Contains the main method. Runs the program.
 * Author:			Ian Nguyen - S3788210
 */

public class Driver {
	// Main method runs the program.
	public static void main(String[] args)
	{
		Menu menu = new Menu();
		menu.run();
	}

}
