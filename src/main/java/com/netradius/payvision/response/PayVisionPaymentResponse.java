package com.netradius.payvision.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Abhinav Nahar
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Data
public class PayVisionPaymentResponse implements Serializable {
	private TransactionStatus transactionStatus;
	@JsonProperty("response")
	private String transactionStatusId;
	@JsonProperty("responsetext")
	private String message;
	@JsonProperty("transactionid")
	private String transactionId;
	@JsonProperty("authcode")
	private String authCode;
	@JsonProperty("avsresponse")
	private String avsResponse;  //TODO
	@JsonProperty("cvvresponse")
	private String cvvresponse;  //TODO
	@JsonProperty(value = "response_code")
	private String responseCode;
	private PayVisionResponseStatus responseStatus;
	@JsonProperty("orderid")
	private String orderid;

	@JsonProperty(value = "response_code")
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public TransactionStatus getTransactionStatus() {
			return TransactionStatus.getTransactionStatusById(getTransactionStatusId());
	}

	public TransactionStatus getResponseStatus() {
		return TransactionStatus.getTransactionStatusById(getTransactionStatusId());
	}
}
