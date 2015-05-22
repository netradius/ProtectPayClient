package com.netradius.payvision.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Abhinav Nahar
 */
@Data
public class PavVisionVoidRequest extends PayVisionRequest {
	@Setter(AccessLevel.NONE)
	@Getter
	private TransactionType type = TransactionType.VOID;
	@JsonProperty("transactionid")
	private String transactionId;
}
