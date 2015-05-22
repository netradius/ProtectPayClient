package com.netradius.payvision.client.exception;

/**
 * @author Abhinav Nahar
 */
public class PayVisionForbiddenException extends PayVisionException {

	private static final long serialVersionUID = -5723562295794965743L;

	public PayVisionForbiddenException() {}

	public PayVisionForbiddenException(String message) {
		super(message);
	}
}