package com.netradius.payvision.exception;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Abhinav Nahar
 */
public class PayVisionBadRequestException extends PayVisionException {

	private static final long serialVersionUID = -7372223617979515476L;

	protected List<Error> errors = new ArrayList<Error>();

	public PayVisionBadRequestException() {}

	public PayVisionBadRequestException(String message) {
		super(message);
	}

	public PayVisionBadRequestException(String message, List<Error> errors) {
		super(message);
		this.errors = errors;
	}

	public List<Error> getErrors() {
		return errors;
	}

	public void setErrors(List<Error> errors) {
		this.errors = errors;
	}
}