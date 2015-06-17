package com.netradius.payvision.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * @author Abhinav Nahar
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PayvisionVoidRequest extends PayvisionRequest {

	@Setter(AccessLevel.NONE)
	@Getter
	private TransactionType type = TransactionType.VOID;

	@JsonProperty("transactionid")
	private String transactionId;
}
