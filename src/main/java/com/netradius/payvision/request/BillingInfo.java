package com.netradius.payvision.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author Abhinav Nahar
 */
@Data
@Accessors(chain = true)
public class BillingInfo implements Serializable{

	@Size(max = 50)
	private String address1;

	@Size(max = 50)
	private String address2;

	@Size(max = 50)
	private String city;

	@Size(max = 2)
	private String state;

	@Size(max = 50)
	private String zip;

	private String country; // ISO 3166 format

	@Size(max = 100)
	private String email;

	@Size(max = 20)
	private String phone;

	@JsonProperty("billing_method")
	private  String billingMethod; //Should be set to 'recurring' to mark payment as a recurring transaction.
}
