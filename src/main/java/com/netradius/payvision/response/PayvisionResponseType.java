package com.netradius.payvision.response;

/**
 * @author Abhinav Nahar
 * @author Erik Jensen
 */
public enum PayvisionResponseType {

	FAILED("3"), APPROVED("1"), DECLINED("2");

	private String value;

	PayvisionResponseType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public static PayvisionResponseType getByValue(String value) {
		switch (value) {
			case "1":
				return APPROVED;
			case "2":
				return DECLINED;
			case "3":
				return FAILED;
			default:
				return null;
		}
	}
}
