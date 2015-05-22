package com.netradius.payvision.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Abhinav Nahar
 */
@Data
public class PayVisionValidateRequest extends PayVisionRequest {
	@Setter(AccessLevel.NONE)
	@Getter
	private TransactionType type = TransactionType.UPDATE;
	private CreditCard creditCard;
	private BillingInfo billingInfo;
	private OrderInfo orderInfo;
	private ShippingInfo shippingInfo;
}
