package com.netradius.payvision.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Abhinav Nahar
 */
@Data
public class OrderInfo {

	@JsonProperty("tax")
	private BigDecimal tax;

	@JsonProperty("ponumber")
	private String purchaseOrderNumber;

	@JsonProperty("orderid")
	private String orderId;

	@JsonProperty("duty_amount")
	private BigDecimal dutyAmount;

	@JsonProperty("discount_amount")
	private BigDecimal discountAmount;

	@JsonProperty("national_tax_amount")
	private BigDecimal nationalTaxAmount;

	@JsonProperty("alternate_tax_amount")
	private BigDecimal alternateTaxAmount;

	@JsonProperty("alternate_tax_id")
	private String alternateTaxId;

	@JsonProperty("vat_tax_amount")
	private BigDecimal vatTaxAmount;

	@JsonProperty("vat_tax_rate")
	private BigDecimal vatTaxRate;

	@JsonProperty("vat_invoice_reference_number")
	private String vatInvoiceReferenceNumber;//	Invoice number that is associated with the VAT invoice.

	@JsonProperty("customer_vat_registration")
	private String customerVatRegistration;//	Value added tax registration number supplied by the cardholder.

	@JsonProperty("merchant_vat_registration")
	private String merchantVatRegistration;

	@JsonProperty("order_date")
	private String orderDate; //YYMMDD

	@JsonProperty("summary_commodity_code")
	private String summaryCommodityCode;

	@JsonProperty("orderdescription")
	private String orderdescription;

}
