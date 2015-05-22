package com.netradius.payvision.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author Abhinav Nahar
 */
@Data
public class PayVisionUpdateRequest extends PayVisionRequest {
	@Setter(AccessLevel.NONE)
	@Getter
	private TransactionType type = TransactionType.UPDATE;
	@JsonProperty(value = "transactionid")
	private String transactionId;
	@JsonProperty(value = "tracking_number")
	private String tracking_number;
	@JsonProperty(value = "shipping")
	private BigDecimal shipping; //shipping amount
	@JsonProperty(value = "shipping_postal")
	private String 	shippingPostal;
	@JsonProperty(value = "ship_from_postal")
	private String shipFromPostal;
	@JsonProperty(value = "shipping_country")
	private String shippingCountry;
	@JsonProperty(value = "shipping_carrier")
	private String shippingCarrier; //valid Values: 'ups', 'fedex', 'dhl', or 'usps'
	@JsonProperty(value = "shipping_date")
	private String shippingDate;
	@JsonProperty(value = "orderdescription")
	private String orderDescription;
	@JsonProperty(value = "order_date")
	private String orderDate;//Format: YYYYMMDD
	@JsonProperty(value = "customer_receipt")
	private String customerReceipt;
	@JsonProperty(value = "ponumber")
	private String purchaseOrderNumber;
	@JsonProperty(value = "summary_commodity_code")
	private String summaryCommodityCode;
	@JsonProperty(value = "duty_amount")
	private BigDecimal dutyAmount;
	@JsonProperty(value = "discount_amount")
	private BigDecimal discountAmount;
	private BigDecimal tax ;
	@JsonProperty(value = "national_tax_amount")
	private BigDecimal nationalTaxAmount;
	@JsonProperty(value = "alternate_tax_amount")
	private BigDecimal 	alternateTaxAmount;
	@JsonProperty(value = "alternate_tax_id")
	private String alternateTaxId;
	@JsonProperty(value = "vat_tax_amount")
	private BigDecimal vatTaxAmount;
	@JsonProperty(value = "vat_tax_rate")
	private BigDecimal vatTaxRate;
	@JsonProperty(value = "vat_invoice_reference_number")
	private String vatInvoiceReferenceNumber;
	@JsonProperty(value = "customer_vat_registration")
	private String customerVatRegistration;
	@JsonProperty(value = "merchant_vat_registration")
	private String merchantVatRegistration;

}
