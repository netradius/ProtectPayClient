package com.netradius.protectpay;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

/**
 * Holds billing details.
 *
 * @author Erik R. Jensen
 */
public class ProtectPayBillingInfo implements Serializable {

	private static final long serialVersionUID = -7562334844020586761L;

	public enum Country {
		USA,
		CAN
	}

	@Size(max = 50)
	private String address1;

	@Size(max = 50)
	private String address2;

	@Size(max = 50)
	private String address3;

	@Size(max = 50)
	private String city;

	@Size(max = 3)
	private String state;

	@Size(max = 50)
	private String zipCode;

	private Country country;

	@Size(max = 100)
	private String emailAddress;

	@Size(max = 20)
	private String telephoneNumber;

	/**
	 * Returns the first line of the address.
	 *
	 * @return the first line of the address
	 */
	public String getAddress1() {
		return address1;
	}

	/**
	 * Sets the first line of the address. This field is optional and the max length is 50.
	 *
	 * @param address1 the first line of the address
	 */
	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	/**
	 * Returns the second line of the address.
	 *
	 * @return the second line of the address
	 */
	public String getAddress2() {
		return address2;
	}

	/**
	 * Sets the second line of the address. This field is optional and the max length is 50.
	 *
	 * @param address2 the second line of the address
	 */
	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	/**
	 * Returns the third line of the address.
	 *
	 * @return the third line of the address
	 */
	public String getAddress3() {
		return address3;
	}

	/**
	 * Sets the third line of the address. This field is optional and the max length is 50.
	 *
	 * @param address3 the third line of the address
	 */
	public void setAddress3(String address3) {
		this.address3 = address3;
	}

	/**
	 * Returns the city on the address.
	 *
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * Sets the city on the address. This field is optional and the max length is 25.
	 *
	 * @param city the city
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * Returns the state on the address.
	 *
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * Sets the state on the address. This field is optional and the max length is 3.
	 *
	 * @param state the state
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * Returns the ZIP code on the address.
	 *
	 * @return the ZIP code
	 */
	public String getZipCode() {
		return zipCode;
	}

	/**
	 * Sets the ZIP code on the address. This field is optional and the max length is 10.
	 *
	 * @param zipCode the ZIP code
	 */
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	/**
	 * Returns the country on the address.
	 *
	 * @return the country
	 */
	public Country getCountry() {
		return country;
	}

	/**
	 * Sets the country on the address. This field is optional.
	 *
	 * @param country the country
	 */
	public void setCountry(Country country) {
		this.country = country;
	}

	/**
	 * Returns the email address.
	 *
	 * @return the email address
	 */
	public String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * Sets the email address. This field is optional and the max length is 100.
	 *
	 * @param emailAddress the email address
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	/**
	 * Returns the telephone number.
	 *
	 * @return the telephone number
	 */
	public String getTelephoneNumber() {
		return telephoneNumber;
	}

	/**
	 * Sets the telephone number. This field is optional and the max length is 10 for the US and 20 of non-US numbers.
	 *
	 * @param telephoneNumber the telephone number
	 */
	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}

	@Override
	public String toString() {
		return "BillingInfo{" +
				"address1='" + address1 + '\'' +
				", address2='" + address2 + '\'' +
				", address3='" + address3 + '\'' +
				", city='" + city + '\'' +
				", state='" + state + '\'' +
				", zipCode='" + zipCode + '\'' +
				", country=" + country +
				", emailAddress='" + emailAddress + '\'' +
				", telephoneNumber='" + telephoneNumber + '\'' +
				'}';
	}

	/**
	 * Performs JSR 303 validation on the object.
	 *
	 * @return constraint violations
	 */
	public Set<ConstraintViolation<ProtectPayBillingInfo>> validate() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		return validator.validate(this, getClass());
	}
}
