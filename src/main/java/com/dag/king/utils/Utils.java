package com.dag.king.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Class that containts several helper functions
 * 
 * @author david.galindo
 *
 */
public class Utils {

	/**
	 * return true if given string is all
	 * 
	 * @param a
	 * @return
	 */
	public static boolean isValidIntegerParam(String a) {
		if (a == null) {
			return false;
		}

		// Integer.MAX_VALUE == 2 147 483 647
		//
		int N = a.length();
		//
		// too long it cannot be a valid integer param
		if (N > 10 || N <= 0) {
			return false;
		}
		//
		for (int i = 0; i < N; i++) {
			char c = a.charAt(i);
			if (c < '0' || c > '9') {
				return false;
			}
		}

		return true;
	}

	/**
	 * Return int representation for given string -1 if param is not a valid
	 * integer
	 * 
	 * @param userId
	 * @return
	 */
	public static int getIntegerParam(String userId) {
		// we will perform a small validation
		if (isValidIntegerParam(userId)) {
			try {
				return Integer.parseInt(userId);
			} catch (Exception ex) {
				return -1;
			}
		} else {
			return -1;
		}
	}

	/**
	 * try to read an integer from given stream, if stream contains is bigger
	 * than expected we will return error if integer is not valid we will return
	 * -1
	 * 
	 * @param requestBody
	 * @return
	 */
	public static int readInputContent(InputStream stream) {
		byte[] buffer = new byte[20];
		try {
			int n = stream.read(buffer);
			if (n > 0) {
				return getIntegerParam(new String(buffer, 0, n));
			} else
				return -1;
		} catch (IOException e) {
			return -1;
		}
	}
}
