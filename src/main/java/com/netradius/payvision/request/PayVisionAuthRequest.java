package com.netradius.payvision.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author Abhinav Nahar
 */
@Data
public class PayVisionAuthRequest extends PayVisionRequest {
	private BigDecimal amount;
	private String currency;  //The transaction currency. Format: ISO 4217
	@Setter(AccessLevel.NONE)
	@Getter
	private TransactionType type = TransactionType.AUTH;
	private CreditCard creditCard;
	private BillingInfo billingInfo;
	private OrderInfo orderInfo;
	private ShippingInfo shippingInfo;
	private PaymentDescriptor paymentDescriptor;
	private PaymentProcessorInfo paymentProcessorInfo;
}
