package com.netradius.payvision.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * @author Abhinav Nahar
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class PayvisionVoidRequest extends PayvisionRequest<PayvisionVoidRequest> {

	@Setter(AccessLevel.NONE)
	@Getter
	private TransactionType type = TransactionType.VOID;

	@JsonProperty("transactionid")
	private String transactionId;
}
