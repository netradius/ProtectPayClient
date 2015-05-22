package com.netradius.payvision.client.exception;

import java.io.IOException;

/**
 * @author Erik R. Jensen
 */
public class PayVisionException extends IOException {

	private static final long serialVersionUID = 2779235624601303153L;

	public PayVisionException() {}

	public PayVisionException(String message) {
		super(message);
	}

	public PayVisionException(String message, Throwable t) {
		super(message, t);
	}

	public PayVisionException(Throwable t) {
		super(t);
	}


}
