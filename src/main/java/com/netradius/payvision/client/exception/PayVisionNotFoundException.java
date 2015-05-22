package com.netradius.payvision.client.exception;

/**
 * @author Abhinav Nahar
 */
public class PayVisionNotFoundException extends PayVisionException {

	private static final long serialVersionUID = -5198771349467795833L;

	public PayVisionNotFoundException() {}

	public PayVisionNotFoundException(String message) {
		super(message);
	}

	public PayVisionNotFoundException(String message, Throwable t) {
		super(message, t);
	}

	public PayVisionNotFoundException(Throwable t) {
		super(t);
	}

}
