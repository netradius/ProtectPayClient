package com.netradius.payvision.request;

import lombok.*;

import java.math.BigDecimal;

/**
 * @author Abhinav Nahar
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PayvisionAuthRequest extends PayvisionRequest {

	private BigDecimal amount;

	private String currency;  //The transaction currency. Format: ISO 4217

	@Setter(AccessLevel.NONE)
	private TransactionType type = TransactionType.AUTH;

	private CreditCard creditCard;

	private BillingInfo billingInfo;

	private OrderInfo orderInfo;

	private ShippingInfo shippingInfo;

	private PaymentDescriptor paymentDescriptor;

	private PaymentProcessorInfo paymentProcessorInfo;
}
