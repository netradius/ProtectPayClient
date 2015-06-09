package com.netradius.payvision.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;

/**
 * @author Abhinav Nahar
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PayvisionRefundRequest extends PayvisionRequest {

	@JsonProperty("transactionid")
	private String transactionId;

	@Setter(AccessLevel.NONE)
	private TransactionType type = TransactionType.REFUND;

	private BigDecimal amount;
}
