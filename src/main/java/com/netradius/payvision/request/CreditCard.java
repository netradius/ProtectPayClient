package com.netradius.payvision.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author Abhinav Nahar
 */
@Data
public class CreditCard extends PaymentType {
	private String ccnumber;
	@JsonProperty("ccexp")
	private String expirationDate; //Date format MMYY
	private String cvv;
}
