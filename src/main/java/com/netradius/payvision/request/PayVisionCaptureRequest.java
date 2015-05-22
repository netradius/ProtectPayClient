package com.netradius.payvision.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author Abhinav Nahar
 */
@Data
public class PayVisionCaptureRequest extends PayVisionRequest {
	private BigDecimal amount;
	@JsonProperty("transactionid")
	private String transactionId;
	@Setter(AccessLevel.NONE)
	@Getter
	private TransactionType type = TransactionType.CAPTURE;

}
