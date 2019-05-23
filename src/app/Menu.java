package app;

import java.util.Scanner;

import exceptions.InvalidBooking;
import exceptions.InvalidBookingFee;
import exceptions.InvalidDate;
import exceptions.InvalidId;
import exceptions.InvalidRefreshments;
import utilities.DateTime;
import utilities.DateUtilities;

/*
 * Class:		Menu
 * Description:	The class a menu and is used to interact with the user. 
 * Author:		Ian Nguyen
 */
public class Menu {
	private Scanner console = new Scanner(System.in);
	private MiRideApplication application = new MiRideApplication();
	// Allows me to turn validation on/off for testing business logic in the
	// classes.
	private boolean testingWithValidation = true;

	/*
	 * Runs the menu in a loop until the user decides to exit the system.
	 */
	public void run() throws InvalidBooking, InvalidRefreshments, InvalidId, InvalidDate, InvalidBookingFee {
		final int MENU_ITEM_LENGTH = 2;
		String input;
		String choice = "";
		System.out.println(application.startUpLoad());
		do {
			printMenu();

			input = console.nextLine().toUpperCase();

			if (input.length() != MENU_ITEM_LENGTH) {
				System.out.println("Error - selection must be two characters!");
			} else {
				System.out.println();

				switch (input) {
				case "CC":
					createCar();
					break;
				case "BC":
					book();
					break;
				case "CB":
					completeBooking();
					break;
				case "DA":
					this.displayAvailable();
					break;
				case "SS":
					System.out.print("Enter Registration Number: ");
					System.out.println(application.displaySpecificCar(console.nextLine()));
					break;
				case "SA":
					this.searchAvailableCars();
					break;
				case "SD":
					application.seedData();
					break;
				case "EX":
					choice = "EX";
					application.saveData();
					System.out.println("Exiting Program ... Goodbye!");
					break;
				default:
					System.out.println("Error, invalid option selected!");
					System.out.println("Please try Again...");
				}
			}

		} while (choice != "EX");
	}

	/*
	 * Creates cars for use in the system available or booking.
	 */
	private void createCar() throws InvalidRefreshments, InvalidId, InvalidBookingFee {
		String id = "", make, model, driverName, serviceType;
		int numPassengers = 0;

		System.out.print("Enter registration number: ");
		id = promptUserForRegNo();
		if (id.length() != 0) {
			// Get details required for creating a car.
			System.out.print("Enter Make: ");
			make = console.nextLine();

			System.out.print("Enter Model: ");
			model = console.nextLine();

			System.out.print("Enter Driver Name: ");
			driverName = console.nextLine();

			try {
				System.out.print("Enter number of passengers: ");
				numPassengers = promptForPassengerNumbers();
			} catch (NumberFormatException e) {
				System.out.println("Error - Input is not a number.\n");
				return;
			}

			System.out.print("Enter service type: ");
			serviceType = console.nextLine();

			boolean result = application.checkIfCarExists(id);

			if (!result && serviceType.equalsIgnoreCase("SD")) {
				String carRegistrationNumber = application.createCar(id, make, model, driverName, numPassengers);
				System.out.println(carRegistrationNumber);
			} else if (!result && serviceType.equalsIgnoreCase("SS")) {
				System.out.print("Enter Standard Fee: ");
				double bookingFee = Double.parseDouble(console.nextLine());
				System.out.print("Enter List of Refreshments: ");
				String[] refreshments = this.promptForRefreshments();
				try {
					String carRegistrationNumber = application.createSSCar(id, make, model, driverName, numPassengers,
							bookingFee, refreshments);
					System.out.println(carRegistrationNumber);
				} catch (InvalidRefreshments e) {
					System.out.print(e.getMessage());
					return;
				} catch (InvalidId e) {
					System.out.print(e.getMessage());
					return;
				} catch (InvalidBookingFee e) {
					System.out.print(e.getMessage());
					return;
				} catch (NullPointerException e) {
					System.out.println("An unknown error has occured.");
				} catch (StringIndexOutOfBoundsException e) {
					System.out.println("Error - No input was entered.");
				} catch(NumberFormatException e) {
					System.out.println("Error - You must enter a number.");
				}

			} else if (!serviceType.equalsIgnoreCase("SD") && !serviceType.equalsIgnoreCase("SS")) {
				System.out.print("Error - Invalid service type.\n");
			} else {
				System.out.println("Error - Already exists in the system \n");
			}
		}
	}

	/*
	 * Book a car by finding available cars for a specified date.
	 */
	private boolean book() throws InvalidBooking, InvalidDate {
		System.out.println("Enter date car required: ");
		System.out.println("(format DD/MM/YYYY)");
		try {
			String dateEntered = console.nextLine();
			int day = Integer.parseInt(dateEntered.substring(0, 2));
			int month = Integer.parseInt(dateEntered.substring(3, 5));
			int year = Integer.parseInt(dateEntered.substring(6));

			DateTime dateRequired = new DateTime(day, month, year);
			
			if (!DateUtilities.dateIsNotInPast(dateRequired)
					|| !DateUtilities.dateIsNotMoreThanXDays(dateRequired, 7)) {
				System.out.println("Error - Date is invalid, must be within the coming week.");
				return false;
			}

			String[] availableCars = application.book(dateRequired);
			for (int i = 0; i < availableCars.length; i++) {
				System.out.println(availableCars[i]);
			}
			if (availableCars.length != 0) {
				System.out.println("Please enter a number from the list:");
				int itemSelected = Integer.parseInt(console.nextLine());

				String regNo = availableCars[itemSelected - 1];
				regNo = regNo.substring(regNo.length() - 6);
				System.out.println("Please enter your first name:");
				String firstName = console.nextLine();
				System.out.println("Please enter your last name:");
				String lastName = console.nextLine();
				System.out.println("Please enter the number of passengers:");
				int numPassengers = Integer.parseInt(console.nextLine());
				try {
					String result = application.book(firstName, lastName, dateRequired, numPassengers, regNo);
					System.out.println(result);
				} catch (InvalidBooking e) {
					System.out.println(e.getMessage());
					return false;
				}

			} else {
				System.out.println("There are no available cars on this date.");
			}
		} catch (NumberFormatException e) {
			System.out.println("Error - A number must be entered. \n");
			return false;
		} catch (InvalidDate e) {
			System.out.println(e.getMessage() + "\n");
			return false;
		} catch (StringIndexOutOfBoundsException e) {
			System.out.println("Error - No input was entered.");
		}
		return true;
	}

	/*
	 * Complete bookings found by either registration number or booking date.
	 */
	private void completeBooking() throws InvalidDate {
		System.out.print("Enter Registration or Booking Date:");
		String response = console.nextLine();

		String result;
		// User entered a booking date
		if (response.contains("/")) {
			System.out.print("Enter First Name:");
			String firstName = console.nextLine();
			System.out.print("Enter Last Name:");
			String lastName = console.nextLine();
			System.out.print("Enter kilometers:");
			double kilometers = Double.parseDouble(console.nextLine());
			int day = Integer.parseInt(response.substring(0, 2));
			int month = Integer.parseInt(response.substring(3, 5));
			int year = Integer.parseInt(response.substring(6));
			DateTime dateOfBooking = new DateTime(day, month, year);
			try {
				result = application.completeBooking(firstName, lastName, dateOfBooking, kilometers);
				System.out.println(result);
			} catch (InvalidDate e) {
				System.out.println(e.getMessage() + "\n");
			}
		} else {
			try {
			System.out.print("Enter First Name:");
			String firstName = console.nextLine();
			System.out.print("Enter Last Name:");
			String lastName = console.nextLine();
			if (application.getBookingByName(firstName, lastName, response)) {
				System.out.print("Enter kilometers:");
				double kilometers = Double.parseDouble(console.nextLine());
				result = application.completeBooking(firstName, lastName, response, kilometers);
				System.out.println(result);
			} else {
				System.out.println("Error: Booking not found.");
			}
			} catch(NumberFormatException e) {
				System.out.println("Error - You must enter a number.");
			}
		}

	}

	private int promptForPassengerNumbers() {
		int numPassengers = 0;
		boolean validPassengerNumbers = false;
		// By pass user input validation.
		if (!testingWithValidation) {
			return Integer.parseInt(console.nextLine());
		} else {
			while (!validPassengerNumbers) {
				numPassengers = Integer.parseInt(console.nextLine());

				String validId = application.isValidPassengerCapacity(numPassengers);
				if (validId.contains("Error:")) {
					System.out.println(validId);
					System.out.println("Enter passenger capacity: ");
					System.out.println("(or hit ENTER to exit)");
				} else {
					validPassengerNumbers = true;
				}
			}
			return numPassengers;
		}
	}

	/*
	 * Prompt user for registration number and validate it is in the correct form.
	 * Boolean value for indicating test mode allows by passing validation to test
	 * program without user input validation.
	 */
	private String promptUserForRegNo() {
		String regNo = "";
		boolean validRegistrationNumber = false;
		// By pass user input validation.
		if (!testingWithValidation) {
			return console.nextLine();
		} else {
			while (!validRegistrationNumber) {
				regNo = console.nextLine();
				boolean exists = application.checkIfCarExists(regNo);
				if (exists) {
					// Empty string means the menu will not try to process
					// the registration number
					System.out.println("Error: Reg Number already exists");
					return "";
				}
				if (regNo.length() == 0) {
					break;
				}

				String validId = application.isValidId(regNo);
				if (validId.contains("Error:")) {
					System.out.println(validId);
					System.out.println("Enter registration number: ");
					System.out.println("(or hit ENTER to exit)");
				} else {
					validRegistrationNumber = true;
				}
			}
			return regNo;
		}
	}

	// Prompts user to enter in refreshments for the silver service car
	private String[] promptForRefreshments() {
		String refreshments = console.nextLine();
		refreshments = refreshments.replaceAll("\\s", "");
		String[] refreshmentsArray;
		String delimiter = ",";
		refreshmentsArray = refreshments.split(delimiter);
		return refreshmentsArray;
	}

	// Displays all the cars of a certain type and sorted.
	private void displayAvailable() {
		System.out.print("Enter Type (SD/SS): ");
		String serviceType = console.nextLine();
		System.out.print("Enter Sort Type (A/D): ");
		String sortType = console.nextLine();
		System.out.println(application.displayAllBookings(serviceType, sortType));
	}

	// Displays all available cars at a given date.
	private void searchAvailableCars() throws InvalidDate {
		System.out.print("Enter type (SD/SS): ");
		String serviceType = console.nextLine();
		if (!serviceType.equalsIgnoreCase("SD") && !serviceType.equalsIgnoreCase("SS")) {
			System.out.print("Error - Invalid service type.\n");
			return;
		}
		try {
			System.out.println("Date: ");
			System.out.println("(format DD/MM/YYYY)");
			String dateEntered = console.nextLine();
			int day = Integer.parseInt(dateEntered.substring(0, 2));
			int month = Integer.parseInt(dateEntered.substring(3, 5));
			int year = Integer.parseInt(dateEntered.substring(6));
			DateTime dateRequired = new DateTime(day, month, year);
			System.out.println(application.searchAvailableCars(serviceType, dateRequired));
		} catch (NumberFormatException e) {
			System.out.println("Error - Date entered is invalid.");
		} catch (InvalidDate e) {
			System.out.println(e.getMessage());
		}
	}

	/*
	 * Prints the menu.
	 */
	private void printMenu() {
		System.out.printf("\n********** MiRide System Menu **********\n\n");

		System.out.printf("%-30s %s\n", "Create Car", "CC");
		System.out.printf("%-30s %s\n", "Book Car", "BC");
		System.out.printf("%-30s %s\n", "Complete Booking", "CB");
		System.out.printf("%-30s %s\n", "Display ALL Cars", "DA");
		System.out.printf("%-30s %s\n", "Search Specific Car", "SS");
		System.out.printf("%-30s %s\n", "Search Available Cars", "SA");
		System.out.printf("%-30s %s\n", "Seed Data", "SD");
		System.out.printf("%-30s %s\n", "Exit Program", "EX");
		System.out.println("\nEnter your selection: ");
		System.out.println("(Hit enter to cancel any operation)");
	}
}
