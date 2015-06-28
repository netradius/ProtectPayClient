package com.netradius.payvision.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author Abhinav Nahar
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class PayvisionRefundRequest extends PayvisionRequest<PayvisionRefundRequest> {

	@JsonProperty("transactionid")
	private String transactionId;

	@Setter(AccessLevel.NONE)
	private TransactionType type = TransactionType.REFUND;

	private BigDecimal amount;
}
