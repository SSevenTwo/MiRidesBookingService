package app;

import cars.Car;
import cars.SilverServiceCar;
import exceptions.InvalidBooking;
import exceptions.InvalidBookingFee;
import exceptions.InvalidDate;
import exceptions.InvalidId;
import exceptions.InvalidRefreshments;
import utilities.DateTime;
import utilities.DateUtilities;
import utilities.MiRidesUtilities;
import java.io.*;
import java.util.Scanner;

/*
 * Class:			MiRideApplication
 * Description:		The system manager the manages the 
 *              	collection of data. 
 * Author:			Ian Nguyen
 */
public class MiRideApplication {
	private Car[] cars = new Car[15];
	private int itemCount = 0;
	private String[] availableCars;

	public MiRideApplication() {
		// seedData();
	}

	// Create Standard Car
	public String createCar(String id, String make, String model, String driverName, int numPassengers)
			throws InvalidId {
		String validId = isValidId(id);
		if (isValidId(id).contains("Error:")) {
			return validId;
		}
		if (!checkIfCarExists(id)) {
			cars[itemCount] = new Car(id, make, model, driverName, numPassengers);
			itemCount++;
			return "New Car added successfully for registion number: " + cars[itemCount - 1].getRegistrationNumber();
		}
		return "Error: Already exists in the system.";
	}

	// Create Silver Service Car
	public String createSSCar(String id, String make, String model, String driverName, int numPassengers,
			double bookingFee, String[] refreshments) throws InvalidRefreshments, InvalidId, InvalidBookingFee {
		String validId = isValidId(id);
		if (isValidId(id).contains("Error:")) {
			return validId;
		}
		if (!checkIfCarExists(id)) {
			cars[itemCount] = new SilverServiceCar(id, make, model, driverName, numPassengers, bookingFee,
					refreshments);
			itemCount++;
			return "New Car added successfully for registion number: " + cars[itemCount - 1].getRegistrationNumber();
		}
		return "Error: Already exists in the system.";
	}

	// Returns an array of available cars on a given date
	public String[] book(DateTime dateRequired) throws InvalidDate {
		int numberOfAvailableCars = 0;
		// finds number of available cars to determine the size of the array required.
		for (int i = 0; i < cars.length; i++) {
			if (cars[i] != null) {
				if (!cars[i].isCarBookedOnDate(dateRequired)) {
					if (!(cars[i] instanceof SilverServiceCar)) {
						numberOfAvailableCars++;
					} else if (DateUtilities.dateIsNotMoreThanXDays(dateRequired, 3)) {
						numberOfAvailableCars++;
					}

				}
			}
		}
		if (numberOfAvailableCars == 0) {
			String[] result = new String[0];
			return result;
		}
		availableCars = new String[numberOfAvailableCars];
		int availableCarsIndex = 0;
		// Populate available cars with registration numbers
		for (int i = 0; i < cars.length; i++) {

			if (cars[i] != null) {
				if (!cars[i].isCarBookedOnDate(dateRequired)) {
					if (!(cars[i] instanceof SilverServiceCar)) {
						availableCars[availableCarsIndex] = availableCarsIndex + 1 + ". "
								+ cars[i].getRegistrationNumber();
						availableCarsIndex++;
					} else if (DateUtilities.dateIsNotMoreThanXDays(dateRequired, 3)) {
						availableCars[availableCarsIndex] = availableCarsIndex + 1 + ". "
								+ cars[i].getRegistrationNumber();
						availableCarsIndex++;
					}
				}
			}
		}
		return availableCars;
	}

	// Books a car
	public String book(String firstName, String lastName, DateTime required, int numPassengers,
			String registrationNumber) throws InvalidBooking {
		Car car = getCarById(registrationNumber);
		if (car != null) {
			if (car.book(firstName, lastName, required, numPassengers)) {

				String message = "Thank you for your booking. \n" + car.getDriverName() + " will pick you up on "
						+ required.getFormattedDate() + ". \n" + "Your booking reference is: "
						+ car.getBookingID(firstName, lastName, required);
				return message;
			} else {
				String message = "Booking could not be completed.";
				return message;
			}
		} else {
			return "Car with registration number: " + registrationNumber + " was not found.";
		}
	}

	// Completes a booking using a specific date.
	public String completeBooking(String firstName, String lastName, DateTime dateOfBooking, double kilometers)
			throws InvalidDate {

		// Search all cars for bookings on a particular date.
		for (int i = 0; i < cars.length; i++) {
			if (cars[i] != null) {
				if (cars[i].isCarBookedOnDate(dateOfBooking)) {
					return cars[i].completeBooking(firstName, lastName, dateOfBooking, kilometers);
				}
			}
		}
		return "Booking not found.";
	}

	// Completes a booking using registration number and name.
	public String completeBooking(String firstName, String lastName, String registrationNumber, double kilometers) {
		String carNotFound = "Car not found";
		Car car = null;
		// Search for car with registration number
		for (int i = 0; i < cars.length; i++) {
			if (cars[i] != null) {
				if (cars[i].getRegistrationNumber().equalsIgnoreCase(registrationNumber)) {
					car = cars[i];
					break;
				}
			}
		}

		if (car == null) {
			return carNotFound;
		}
		if (car.getBookingByName(firstName, lastName) != -1) {
			return car.completeBooking(firstName, lastName, kilometers);
		}
		return "Error: Booking not found.";
	}

	// Searches for a booking associated to a name.
	public boolean getBookingByName(String firstName, String lastName, String registrationNumber) {
		Car car = null;
		// Search for car with registration number
		for (int i = 0; i < cars.length; i++) {
			if (cars[i] != null) {
				if (cars[i].getRegistrationNumber().equalsIgnoreCase(registrationNumber)) {
					car = cars[i];
					break;
				}
			}
		}

		if (car == null) {
			return false;
		}
		if (car.getBookingByName(firstName, lastName) == -1) {
			return false;
		}
		return true;
	}

	// Displays a specific car using registration number.
	public String displaySpecificCar(String regNo) {
		for (int i = 0; i < cars.length; i++) {
			if (cars[i] != null) {
				if (cars[i].getRegistrationNumber().equalsIgnoreCase(regNo)) {
					return cars[i].getDetails();
				}
			}
		}
		return "Error: The car could not be located.";
	}

	// Seed data method
	public boolean seedData() throws InvalidBooking, InvalidRefreshments, InvalidId, InvalidBookingFee {
		for (int i = 0; i < cars.length; i++) {
			if (cars[i] != null) {
				return false;
			}
		}
		// 2 cars not booked
		Car honda = new Car("SIM194", "Honda", "Accord Euro", "Henry Cavill", 5);
		cars[itemCount] = honda;
		honda.book("Craig", "Cocker", new DateTime(1), 3);
		itemCount++;

		Car lexus = new Car("LEX666", "Lexus", "M1", "Angela Landsbury", 3);
		cars[itemCount] = lexus;
		lexus.book("Craig", "Cocker", new DateTime(1), 3);
		itemCount++;

		// 2 cars booked
		Car bmw = new Car("BMW256", "Mini", "Minor", "Barbara Streisand", 4);
		cars[itemCount] = bmw;
		itemCount++;
		bmw.book("Craig", "Cocker", new DateTime(1), 3);

		Car audi = new Car("AUD765", "Mazda", "RX7", "Matt Bomer", 6);
		cars[itemCount] = audi;
		itemCount++;
		audi.book("Rodney", "Cocker", new DateTime(1), 4);

		// 1 car booked five times (not available)
		Car toyota = new Car("TOY765", "Toyota", "Corola", "Tina Turner", 7);
		cars[itemCount] = toyota;
		itemCount++;
		toyota.book("Rodney", "Cocker", new DateTime(1), 3);
		toyota.book("Craig", "Cocker", new DateTime(2), 7);
		toyota.book("Alan", "Smith", new DateTime(3), 3);
		toyota.book("Carmel", "Brownbill", new DateTime(4), 7);
		toyota.book("Paul", "Scarlett", new DateTime(5), 7);

		// 1 car booked five times (not available)
		Car rover = new Car("ROV465", "Honda", "Rover", "Jonathon Ryss Meyers", 7);
		cars[itemCount] = rover;
		itemCount++;
		rover.book("Rodney", "Cocker", new DateTime(1), 3);
		// rover.completeBooking("Rodney", "Cocker", 75);
		DateTime inTwoDays = new DateTime(2);
		rover.book("Rodney", "Cocker", inTwoDays, 3);
		rover.completeBooking("Rodney", "Cocker", inTwoDays, 75);

		// 2 silver car that have not been booked
		Car nyzmo = new SilverServiceCar("ARO121", "Nyzmo", "Sphere", "Johnny Gustyk", 4, 4.5,
				new String[] { "Monster Energy", "Toilet Paper", "Banana" });
		cars[itemCount] = nyzmo;
		itemCount++;

		Car lexo = new SilverServiceCar("RIP331", "Nissan", "Lancer", "Josh Sammut", 4, 6.0,
				new String[] { "Apple Juice", "Up N Go", "Grapes" });
		cars[itemCount] = lexo;
		itemCount++;

		// 2 silver car that have been booked but not completed
		Car zetsuga = new SilverServiceCar("PRO908", "Lucian", "Lulu", "Huy Tran", 7, 5.0,
				new String[] { "HP Potion", "MP Potion", "SP Potion" });
		cars[itemCount] = zetsuga;
		itemCount++;
		zetsuga.book("Joe", "Carbone", new DateTime(1), 5);

		Car emoli = new SilverServiceCar("EMU888", "Vespa", "Roadster", "Emily Nguyen", 2, 8.0,
				new String[] { "Sting Energy", "FuzeTea", "Lollipop" });
		cars[itemCount] = emoli;
		itemCount++;
		emoli.book("John", "Doe", new DateTime(1), 1);

		// 2 silver car with completed booking
		Car zed = new SilverServiceCar("ZED321", "Floop", "Rover", "Daniel Nguyen", 7, 5.0,
				new String[] { "Chocolate", "5 Gum", "Pineapples" });
		cars[itemCount] = zed;
		itemCount++;
		zed.book("Paul", "Millar", new DateTime(2), 3);

		Car jos = new SilverServiceCar("NOB541", "Walker", "Onfooteru", "Josiah Miranda", 4, 3.8,
				new String[] { "Chocolate", "5 Gum", "Pineapples" });
		cars[itemCount] = jos;
		itemCount++;
		DateTime inTwoDays1 = new DateTime(2);
		jos.book("Goku", "Son", inTwoDays1, 3);
		jos.completeBooking("Goku", "Son", inTwoDays1, 75);
		return true;
	}

	// Displays all cars of a specific type and sorts it accordingly.
	public String displayAllBookings(String serviceType, String sortType) {
		if (itemCount == 0) {
			return "No cars of this type have been added to the system.";
		}
		if (serviceType.equalsIgnoreCase("SD")) {
			if (sortType.equalsIgnoreCase("A")) {
				String[] regNo = this.createArrayOfRegNo();
				return this.sortSdSmallest(regNo);
			}
			if (sortType.equalsIgnoreCase("D")) {
				String[] regNo = this.createArrayOfRegNo();
				return this.sortSdBiggest(regNo);
			}
		}

		if (serviceType.equalsIgnoreCase("SS")) {
			if (sortType.equalsIgnoreCase("A")) {
				String[] regNo = this.createArrayOfRegNo();
				return this.sortSsSmallest(regNo);
			}
			if (sortType.equalsIgnoreCase("D")) {
				String[] regNo = this.createArrayOfRegNo();
				return this.sortSsBiggest(regNo);
			}
		}
		return "Error";
	}

	// Displays all bookings using a specfic ID.
	public String displayBooking(String id, String seatId) {
		Car booking = getCarById(id);
		if (booking == null) {
			return "Booking not found";
		}
		return booking.getDetails();
	}

	// Checks if RegNo is valid.
	public String isValidId(String id) {
		return MiRidesUtilities.isRegNoValid(id);
	}

	// Checks if Passenger Number is valid.
	public String isValidPassengerCapacity(int passengerNumber) {
		return MiRidesUtilities.isPassengerCapacityValid(passengerNumber);
	}

	// Checks if car exists.
	public boolean checkIfCarExists(String regNo) {
		Car car = null;
		if (regNo.length() != 6) {
			return false;
		}
		car = getCarById(regNo);
		if (car == null) {
			return false;
		} else {
			return true;
		}
	}

	// Get a specific car using regNo
	private Car getCarById(String regNo) {
		Car car = null;

		for (int i = 0; i < cars.length; i++) {
			if (cars[i] != null) {
				if (cars[i].getRegistrationNumber().equalsIgnoreCase(regNo)) {
					car = cars[i];
					return car;
				}
			}
		}
		return car;
	}

	// Search for available cars from given date and service type.
	public String searchAvailableCars(String serviceType, DateTime dateRequired) throws InvalidDate {
		StringBuilder sb = new StringBuilder();
		boolean carFound = false;
		if (serviceType.equalsIgnoreCase("SS")) {
			for (Car car : this.cars) {
				if (!(car == null)) {
					if (car instanceof SilverServiceCar) {
						if (!(car.isCarBookedOnDate(dateRequired))) {
							sb.append(car.getDetails());
							carFound = true;
						}
					}
				}
			}
			if (carFound == true) {
				return sb.toString();
			} else {
				sb.append("Error - No cars were found on this date.");
				return sb.toString();
			}
		}
		if (serviceType.equalsIgnoreCase("SD")) {
			for (Car car : this.cars) {
				if (!(car == null)) {
					if (!(car instanceof SilverServiceCar)) {
						if (!(car.isCarBookedOnDate(dateRequired))) {
							sb.append(car.getDetails());
							carFound = true;
						}
					}
				}
			}
			if (carFound == true) {
				return sb.toString();
			} else {
				sb.append("Error - No cars were found on this date.");
				return sb.toString();
			}
		} else {
			sb.append("Error - No cars were found on this date.");
			return sb.toString();
		}
	}

	// Returns an array of registration numbers.
	public String[] createArrayOfRegNo() {
		String[] regNoArray = new String[15];
		for (int i = 0; i < cars.length; i++) {
			if (!(cars[i] == null)) {
				regNoArray[i] = cars[i].getRegistrationNumber();
			}
		}
		return regNoArray;
	}

	// Sorts the standard cars from largest to smallest.
	public String sortSdBiggest(String[] regNo) {
		StringBuilder sb = new StringBuilder();
		sb.append("\nThe following cars are available.\n");
		MiRidesUtilities.sortBig(regNo);
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				if (!(regNo[i] == null) && !(cars[j] == null)) {
					if (cars[j].getRegistrationNumber() == regNo[i] && !(cars[j] instanceof SilverServiceCar)) {
						sb.append(cars[j].getDetails());
					}
				}
			}
		}
		return sb.toString();
	}

	// Sorts the standard cars from smallest to largest.
	public String sortSdSmallest(String[] regNo) {
		StringBuilder sb = new StringBuilder();
		sb.append("\nThe following cars are available.\n");
		MiRidesUtilities.sort(regNo);
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				if (!(regNo[i] == null) && !(cars[j] == null)) {
					if (cars[j].getRegistrationNumber() == regNo[i] && !(cars[j] instanceof SilverServiceCar)) {
						sb.append(cars[j].getDetails());
					}
				}
			}
		}
		return sb.toString();
	}

	// Sorts the Silver Service cars from largest to smallest.
	public String sortSsBiggest(String[] regNo) {
		StringBuilder sb = new StringBuilder();
		sb.append("\nThe following cars are available.\n");
		MiRidesUtilities.sortBig(regNo);
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				if (!(cars[j] == null) && !(regNo[i] == null)) {
					if (cars[j].getRegistrationNumber() == regNo[i] && cars[j] instanceof SilverServiceCar) {
						sb.append(cars[j].getDetails());
					}
				}
			}
		}
		return sb.toString();
	}

	// Sorts the Silver Service cars from smallest to largest.
	public String sortSsSmallest(String[] regNo) {
		StringBuilder sb = new StringBuilder();
		sb.append("\nThe following cars are available.\n");
		MiRidesUtilities.sort(regNo);
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				if (!(cars[j] == null) && !(regNo[i] == null)) {
					if (cars[j].getRegistrationNumber() == regNo[i] && cars[j] instanceof SilverServiceCar) {
						sb.append(cars[j].getDetails());
					}
				}
			}
		}
		return sb.toString();
	}

	// Method which initiates the saving of program data to the system.
	public void saveData() {
		String fileOne = "carData.txt";
		String fileTwo = "carDataBackup.txt";
		this.writeToFile(fileOne);
		this.writeToFile(fileTwo);
	}

	// Method which writes the car details into a file.
	public void writeToFile(String fileName) {
		PrintWriter outputStream = null;
		try {
			outputStream = new PrintWriter(new FileOutputStream(fileName));
			outputStream.write(saveSeed());

		} catch (FileNotFoundException e) {
			System.out.println("Error - File was not found.");
		}
		outputStream.close();
	}

	// Method which returns a list of car details in toString format.
	public String saveSeed() {
		StringBuilder sb = new StringBuilder();
		for (Car car : cars) {
			if (!(car == null)) {
				sb.append(car.stringToSave() + "\n");
			}
		}
		return sb.toString();
	}

	// Initial start up method, that locates any saved local files and loads it into
	// the program.
	public String startUpLoad() throws InvalidRefreshments, NumberFormatException, InvalidId, InvalidBookingFee {
		String a = "";
		if (this.loadFile("carData.txt")) {
			a = "Data sucessfully loaded.";
		}
		return a;
	}

	// Loads the local file into the system and creates information needed to make
	// the saved cars.
	public boolean loadFile(String fileName)
			throws InvalidRefreshments, NumberFormatException, InvalidId, InvalidBookingFee {
		Scanner reader = null;
		int i = 0;
		String[] carInfo = new String[15];
		String data;
		try {
			reader = new Scanner(new File(fileName));
			reader.useDelimiter(":");
		} catch (Exception e) {
			try {
				reader = new Scanner(new File("carDataBackup.txt"));
				System.out.println("Backup data found.");
			} catch (Exception z) {
				System.out.println("No data was loaded.");
				return false;
			}
		}
		while (reader.hasNextLine()) {
			data = reader.nextLine();
			carInfo[i] = data;
			i++;
		}
		reader.close();
		this.makeLoadedCars(carInfo);
		return true;
	}

	// Creates the cars from the loaded files information.
	public void makeLoadedCars(String[] carInfo)
			throws InvalidRefreshments, NumberFormatException, InvalidId, InvalidBookingFee {
		Car temp;
		for (String car : carInfo) {
			if (car != null) {
				String[] carVariables = car.split(":");
				if (carVariables[0].equalsIgnoreCase("SDCAR")) {
					temp = new Car(carVariables[1], carVariables[2], carVariables[3], carVariables[4],
							Integer.parseInt(carVariables[5]));
					this.cars[itemCount] = temp;
					itemCount++;
				} else if (carVariables[0].equalsIgnoreCase("SSCAR")) {
					int noOfItems = carVariables.length - 8;
					String[] refreshments = new String[noOfItems];
					for (int i = 0, j = 8; i < refreshments.length; i++, j++) {
						refreshments[i] = carVariables[j].substring(7);
					}
					temp = new SilverServiceCar(carVariables[1], carVariables[2], carVariables[3], carVariables[4],
							Integer.parseInt(carVariables[5]), Double.parseDouble(carVariables[7]), refreshments);
					this.cars[itemCount] = temp;
					itemCount++;
				}
			}
		}

	}
}
