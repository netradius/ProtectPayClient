package com.netradius.payvision;

import java.io.IOException;

/**
 * Thrown when an error occurs talking to Payvision.
 *
 * @author Erik R. Jensen
 */
public class PayvisionException extends IOException {

	private int httpStatus;

	public PayvisionException(int httpStatus, String msg, Throwable t) {
		super(msg, t);
		this.httpStatus = httpStatus;
	}

	public int getHttpStatus() {
		return httpStatus;
	}
}
