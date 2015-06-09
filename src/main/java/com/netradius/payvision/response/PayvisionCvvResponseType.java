package com.netradius.payvision.response;

/**
 * CVV response values which can be passed back from Payvsion.
 *
 * @author Erik R. Jensen
 */
public enum PayvisionCvvResponseType {

	MATCH("M"),
	NO_MATCH("N"),
	NOT_PROCESSED("P"),
	NOT_PRESENT("S"),
	NOT_CERTIFIED("U");

	private String value;

	PayvisionCvvResponseType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public static PayvisionCvvResponseType getByValue(String value) {
		switch (value) {
			case "M":
				return MATCH;
			case "N":
				return NO_MATCH;
			case "P":
				return NOT_PROCESSED;
			case "S":
				return NOT_PRESENT;
			case "U":
				return NOT_CERTIFIED;
			default:
				return null;
		}
	}
}
