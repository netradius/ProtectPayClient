package com.netradius.payvision.request;

import lombok.*;

/**
 * @author Abhinav Nahar
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PayvisionValidateRequest extends PayvisionRequest {

	@Setter(AccessLevel.NONE)
	private TransactionType type = TransactionType.UPDATE;

	private CreditCard creditCard;

	private BillingInfo billingInfo;

	private OrderInfo orderInfo;

	private ShippingInfo shippingInfo;
}
