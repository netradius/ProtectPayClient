package com.netradius.protectpay;

import javax.validation.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

/**
 * Holds credit card override values. These values will override the values on a
 * payment method when used to make a payment.
 *
 * @author Erik R. Jensen
 */
public class CreditCardOverride implements Serializable {

	private static final long serialVersionUID = -60541015419445010L;

	@Valid
	private ProtectPayBillingInfo billing;

	@Size(max = 4)
	private String cvv;

	@Size(max = 4)
	private String expiration;

	@Size(max = 50)
	private String fullName;

	/**
	 * Returns the billing information.
	 *
	 * @return the billing information
	 */
	public ProtectPayBillingInfo getBilling() {
		return billing;
	}

	/**
	 * Sets the billing information. This is optional.
	 *
	 * @param billing the billing information
	 */
	public void setBilling(ProtectPayBillingInfo billing) {
		this.billing = billing;
	}

	/**
	 * Returns the CVV.
	 *
	 * @return the CVV
	 */
	public String getCvv() {
		return cvv;
	}

	/**
	 * Sets the CVV. This field is optional and the max length is 4.
	 *
	 * @param cvv the CVV
	 */
	public void setCvv(String cvv) {
		this.cvv = cvv;
	}

	/**
	 * Returns the expiration date.
	 *
	 * @return the expiration date
	 */
	public String getExpiration() {
		return expiration;
	}

	/**
	 * Sets the expiration date. This field is optional and the max length is 4.
	 *
	 * @param expiration the expiration date
	 */
	public void setExpiration(String expiration) {
		this.expiration = expiration;
	}

	/**
	 * Returns the full name.
	 *
	 * @return the full name
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * Sets the full name. This field is optional and the max length is 50. This is
	 * the name on the card being processed.
	 *
	 * @param fullName the full name.
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@Override
	public String toString() {
		return "CreditCardOverride{" +
				"billing=" + billing +
				", cvv='" + cvv + '\'' +
				", expiration='" + expiration + '\'' +
				", fullName='" + fullName + '\'' +
				'}';
	}

	/**
	 * Performs JSR 303 validation on the object.
	 *
	 * @return constraint violations
	 */
	public Set<ConstraintViolation<CreditCardOverride>> validate() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		return validator.validate(this, getClass());
	}
}
