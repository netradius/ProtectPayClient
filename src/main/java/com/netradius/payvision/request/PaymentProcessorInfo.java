package com.netradius.payvision.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author Abhinav Nahar
 */
@Data
public class PaymentProcessorInfo {
	@JsonProperty("processor_id")
	private String processorId;

	@JsonProperty("dup_seconds")
	private Integer dupSeconds;
}
