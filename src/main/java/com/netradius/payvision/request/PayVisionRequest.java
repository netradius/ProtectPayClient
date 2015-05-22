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
public class PayVisionRequest {
	@Setter(AccessLevel.NONE)
	@Getter
	protected TransactionType type;
	@JsonProperty("first_name")
	private String firstName;
	@JsonProperty("last_name")
	private String lastName;
	@Setter(AccessLevel.NONE)
	@JsonProperty("payment")
	private String payment = "creditcard"; //TODO currently check is not supported


}
