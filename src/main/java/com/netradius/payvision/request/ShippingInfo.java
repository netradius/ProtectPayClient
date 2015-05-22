package com.netradius.payvision.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Abhinav Nahar
 */
@Data
public class ShippingInfo {
	@JsonProperty("shipping_firstname")
	private String shippingFirstName;
	@JsonProperty("shipping_lastname")
	private String shippingLastName;
	@JsonProperty("shipping_company")
	private String shippingCompany;
	@JsonProperty("shipping_address1")
	private String shippingAddress1;
	@JsonProperty("shipping_address2")
	private String shippingAddress2;
	@JsonProperty("shipping_city")
	private String shippingCity;
	@JsonProperty("shipping_state")
	private String shippingState; //  Format: CC
	@JsonProperty("shipping_zip")
	private String shippingZip;
	@JsonProperty("shipping_country")
	private String shippingCountry; // ISO 3166. Format: CC
	@JsonProperty("shipping_email")
	private String shippingEmail;
	@JsonProperty("shipping")
	private BigDecimal shippingAmount;  //Freight or shipping amount included in the transaction amount
	@JsonProperty("shipping_postal")
	private String shippingPostal;
	@JsonProperty("ship_from_postal")
	private String shipFromPostal;
}