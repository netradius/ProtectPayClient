package com.netradius.payvision.request;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * @author Abhinav Nahar
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class PayvisionValidateRequest extends PayvisionRequest<PayvisionValidateRequest> {

	@Setter(AccessLevel.NONE)
	private TransactionType type = TransactionType.UPDATE;

	private CreditCard creditCard;

	private BillingInfo billingInfo;

	private OrderInfo orderInfo;

	private ShippingInfo shippingInfo;
}
