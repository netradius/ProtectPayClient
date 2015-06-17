package com.netradius.payvision.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Abhinav Nahar
 */
@Data
public class PayvisionRequest<T>  {

	@Setter(AccessLevel.NONE)
	protected TransactionType type;

	@JsonProperty("first_name")
	@Setter(AccessLevel.NONE)
	private String firstName;

	@JsonProperty("last_name")
	@Setter(AccessLevel.NONE)
	private String lastName;

	@Setter(AccessLevel.NONE)
	@JsonProperty("payment")
	private String payment = "creditcard"; //TODO currently check is not supported

	@SuppressWarnings("unchecked")
	public T setFirstName(String firstName) {
		this.firstName = firstName;
		return (T)this;
	}

	@SuppressWarnings("unchecked")
	public T setLastName(String lastName) {
		this.lastName = lastName;
		return (T)this;
	}
}
