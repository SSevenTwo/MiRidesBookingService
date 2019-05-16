package utilities;

import java.util.Arrays;

public class MiRidesUtilities {
	private final static int ID_LENGTH = 6;

	public static String isRegNoValid(String regNo) {
		int regNoLength = regNo.length();
		if (regNoLength != ID_LENGTH) {
			return "Error: registration number must be 6 characters";
		}
		boolean letters = regNo.substring(0, 3).matches("[a-zA-Z]+");
		if (!letters) {
			return "Error: The registration number should begin with three alphabetical characters.";
		}
		boolean numbers = regNo.substring(3).matches("[0-9]+");
		if (!numbers) {
			return "Error: The registration number should end with three numeric characters.";
		}
		return regNo;
	}

	public static String isPassengerCapacityValid(int passengerCapacity) {
		if (passengerCapacity > 0 && passengerCapacity < 10) {
			return "OK";
		} else {
			return "Error: Passenger capacity must be between 1 and 9.";
		}
	}

	// Sorting strings by biggest

	public static String biggest(String[] array) {
		String biggest = array[0];
		for (String a : array) {
			if (!(a == null)) {
				if (biggest.compareToIgnoreCase(a) < 0) {
					biggest = a;
				}
			}
		}
		return biggest;
	}

	public static String biggestInRange(String[] array, int range) {
		int i = range;
		String biggest = array[range];
		while (i < array.length) {
			if (!(array[i] == null)) {
				if (biggest.compareToIgnoreCase(array[i]) < 0) {
					biggest = array[i];
				}

			}
			i++;
		}
		return biggest;
	}

	public static int indexOfTheBiggest(String[] array) {

		return indexOfTheBiggestStartingFrom(array, 0);
	}

	public static int indexOfTheBiggestStartingFrom(String[] array, int index) {
		int i = index;
		while (i < array.length) {
			if (!(array[i] == null)) {
				if (array[i] == biggestInRange(array, index)) {
					break;
				}

			}
			i++;
		}
		return i;
	}

	public static String[] sortBig(String[] array) {
		int i = 0;
		while (i < array.length - 1) {
			swap(array, i, indexOfTheBiggestStartingFrom(array, i));
			i++;
		}
		return array;
	}

	// Sorting strings by smallest

	public static String smallest(String[] array) {
		String smallest = array[0];
		for (String a : array) {
			if (!(a == null)) {
				if (smallest.compareToIgnoreCase(a) > 0) {
					smallest = a;
				}
			}
		}
		return smallest;
	}

	public static String smallestInRange(String[] array, int range) {
		int i = range;
		String smallest = array[range];
		while (i < array.length) {
			if (!(array[i] == null)) {
				if (smallest.compareToIgnoreCase(array[i]) > 0) {
					smallest = array[i];
				}

			}
			i++;
		}
		return smallest;
	}

	public static int indexOfTheSmallest(String[] array) {

		return indexOfTheSmallestStartingFrom(array, 0);
	}

	public static int indexOfTheSmallestStartingFrom(String[] array, int index) {
		int i = index;
		while (i < array.length) {
			if (!(array[i] == null)) {
				if (array[i] == smallestInRange(array, index)) {
					break;
				}

			}
			i++;
		}
		return i;
	}

	public static void swap(String[] array, int index1, int index2) {
		if (index1 == 15 || index2 == 15) {
			return;
		}
		String a = array[index1];
		String b = array[index2];
		array[index1] = b;
		array[index2] = a;
	}

	public static String[] sort(String[] array) {
		int i = 0;
		while (i < array.length - 1) {
			swap(array, i, indexOfTheSmallestStartingFrom(array, i));
			i++;
		}
		return array;
	}

}
