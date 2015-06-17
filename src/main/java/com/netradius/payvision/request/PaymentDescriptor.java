package com.netradius.payvision.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Abhinav Nahar
 */
@Data
@Accessors(chain = true)
public class PaymentDescriptor {

	@JsonProperty("descriptor")
	private String descriptor;

	@JsonProperty("descriptor_phone")
	private String descriptorPhone;

	@JsonProperty("descriptor_address")
	private String descriptorAddress;

	@JsonProperty("descriptor_city")
	private String descriptorCity;

	@JsonProperty("descriptor_state")
	private String descriptorState;

	@JsonProperty("descriptor_postal")
	private String descriptorPostal;

	@JsonProperty("descriptor_country")
	private String descriptorCountry;

	@JsonProperty("descriptor_mcc")
	private String descriptorMcc;

	@JsonProperty("descriptor_merchant_id")
	private String descriptorMerchantId;

	@JsonProperty("descriptor_url")
	private String descriptionUrl;

	@JsonProperty("dup_seconds")
	private int dup_seconds;
	// dup_seconds	Sets the time in seconds for duplicate transaction checking on supported processors. Set to 0
	// to disable duplicate checking.

}
