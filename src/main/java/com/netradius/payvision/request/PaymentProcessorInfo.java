package com.netradius.payvision.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Abhinav Nahar
 */
@Data
@Accessors(chain = true)
public class PaymentProcessorInfo {

	@JsonProperty("processor_id")
	private String processorId;

	@JsonProperty("dup_seconds")
	private Integer dupSeconds;
}
