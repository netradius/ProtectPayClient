package com.netradius.payvision.request;

import lombok.*;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author Abhinav Nahar
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class PayvisionSaleRequest extends PayvisionRequest<PayvisionSaleRequest> {

	private BigDecimal amount;

	private String currency;  //The transaction currency. Format: ISO 4217

	@Setter(AccessLevel.NONE)
	private TransactionType type = TransactionType.SALE;

	private CreditCard creditCard;

//	@JsonProperty("order_id")
//	private String orderID;

	private BillingInfo billingInfo;

	private OrderInfo orderInfo;

	private ShippingInfo shippingInfo;
}
