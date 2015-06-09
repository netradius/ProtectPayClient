package com.netradius.payvision.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Holds response data form a Payvision payment transaction.
 *
 * @author Abhinav Nahar
 * @author Erik Jensen
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Data
public class PaycisionPaymentResponse implements Serializable {

	@JsonProperty("response")
	private String response;

	@JsonProperty("responsetext")
	private String responseText;

	@JsonProperty("authcode")
	private String authCode;

	@JsonProperty("transactionid")
	private String transactionId;

	@JsonProperty("avsresponse")
	private String avsResponse;  //TODO

	@JsonProperty("cvvresponse")
	private String cvvResponse;  //TODO

	@JsonProperty("orderid")
	private String orderId;

	@JsonProperty(value = "response_code")
	private String responseCode;

	public boolean isApproved() {
		return "1".equals(response);
	}

	public boolean isDeclined() {
		return "2".equals(response);
	}

	public boolean isError() {
		return "3".equals(response);
	}

	public PayvisionResponseType getResponseType() {
		return response != null ? PayvisionResponseType.getByValue(response) : null;
	}

	public PayvisionResponseCodeType getResponseCodeType() {
		return responseCode != null ? PayvisionResponseCodeType.getByValue(responseCode) : null;
	}

	public PayvisionCvvResponseType getCvvResponseCode() {
		return cvvResponse != null ? PayvisionCvvResponseType.getByValue(cvvResponse) : null;
	}

}
