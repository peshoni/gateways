package com.pepe.gateways.task.utils;

import java.util.Random;

public class GatesUtility {

	private GatesUtility() {
	}

	/**
	 * Method of generating random number.
	 * 
	 * @param min from
	 * @param max to
	 * @return Generated number.
	 */
	public static int getRandomNumberUsingNextInt(int min, int max) {
		Random random = new Random();
		return random.nextInt(max - min) + min;
	}

	/**
	 * A method of generating random strings.
	 * 
	 * @param size Specifies the length of the string.
	 * @return Generates random string with given length.
	 */
	public static String generatesString(final int size) {
		final String source = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		StringBuilder builder = new StringBuilder();
		for (int index = 0; index < size; index++) {
			builder.append(source.charAt(getRandomNumberUsingNextInt(0, source.length())));
		}
		return builder.toString();
	}

}
