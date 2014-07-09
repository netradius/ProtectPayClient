package com.netradius.protectpay;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

/**
 * Holds payment transaction data.
 *
 * @author Erik R. Jensen
 */
public class ProtectPayPayment implements Serializable {

	private static final long serialVersionUID = -3592399706967798790L;

	@Size(max = 16)
	private String payerAccountId;

	@Size(max = 36)
	private String paymentMethodId;

	private Long merchantProfileId;

	private Integer amount;

	@Size(max = 128)
	private String comment1;

	@Size(max = 128)
	private String comment2;

	@Size(max = 3)
	private String currencyCode;

	private String inputIpAddress;

	@Size(max = 50)
	private String invoice;

	/**
	 * Returns the payer account ID.
	 *
	 * @return the payer account ID
	 */
	public String getPayerAccountId() {
		return payerAccountId;
	}

	/**
	 * Sets the payer account ID.  This field is required and the max length is 16.
	 *
	 * @param payerAccountId the payer account ID
	 */
	public void setPayerAccountId(String payerAccountId) {
		this.payerAccountId = payerAccountId;
	}

	/**
	 * Returns the payment method ID.
	 *
	 * @return the payment method ID
	 */
	public String getPaymentMethodId() {
		return paymentMethodId;
	}

	/**
	 * Sets the payment method ID. This field is required and the max length is 36.
	 *
	 * @param paymentMethodId the payment method ID.
	 */
	public void setPaymentMethodId(String paymentMethodId) {
		this.paymentMethodId = paymentMethodId;
	}

	/**
	 * Returns the merchant profile ID.
	 *
	 * @return the merchant profile ID
	 */
	public Long getMerchantProfileId() {
		return merchantProfileId;
	}

	/**
	 * Sets the merchant profile ID. This fiels is only required if you have setup multiple merchant accounts.
	 *
	 * @param merchantProfileId the merchant profile ID
	 */
	public void setMerchantProfileId(Long merchantProfileId) {
		this.merchantProfileId = merchantProfileId;
	}

	/**
	 * Returns the amount of this payment. This is the amount with no decimal place. In the US this represents
	 * the number of pennies.
	 *
	 * @return the amount of this payment
	 */
	public Integer getAmount() {
		return amount;
	}

	/**
	 * Sets the amount of this payment. This is the amount with no decimal places. In the US this represents
	 * the number of pennies.
	 *
	 * @param amount the amount of this payment
	 */
	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	/**
	 * Returns the first comment on the payment.
	 *
	 * @return the first comment
	 */
	public String getComment1() {
		return comment1;
	}

	/**
	 * Sets the first comment on the payment. This is an optional parameter and the max length is 128.
	 *
	 * @param comment1 the first comment
	 */
	public void setComment1(String comment1) {
		this.comment1 = comment1;
	}

	/**
	 * Returns the second comment on the payment.
	 *
	 * @return the second comment
	 */
	public String getComment2() {
		return comment2;
	}

	/**
	 * Sets the second comment on the payment. This is an optional parameter and the max length is 128.
	 *
	 * @param comment2 the second comment
	 */
	public void setComment2(String comment2) {
		this.comment2 = comment2;
	}

	/**
	 * Returns the currency code.
	 *
	 * @return the currency code
	 */
	public String getCurrencyCode() {
		return currencyCode;
	}

	/**
	 * Sets the currency code. This is a required field whose max length is 3.
	 *
	 * @param currencyCode the currency code
	 */
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	/**
	 * Returns the IP address associated with this payment. This is the IP address of the customer.
	 *
	 * @return the IP address
	 */
	public String getInputIpAddress() {
		return inputIpAddress;
	}

	/**
	 * Sets the IP address associated with this payment. This is the IP address of the customer.
	 *
	 * @param inputIpAddress the IP address
	 */
	public void setInputIpAddress(String inputIpAddress) {
		this.inputIpAddress = inputIpAddress;
	}

	/**
	 * Returns the invoice number.
	 *
	 * @return the invoice number
	 */
	public String getInvoice() {
		return invoice;
	}

	/**
	 * Sets the invoice number. This is an optional parameter whose max length is 50 characters.
	 *
	 * @param invoice the invoice number
	 */
	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}

	@Override
	public String toString() {
		return "Payment{" +
				"payerAccountId='" + payerAccountId + '\'' +
				", paymentMethodId='" + paymentMethodId + '\'' +
				", merchantProfileId='" + merchantProfileId + '\'' +
				", amount=" + amount +
				", comment1='" + comment1 + '\'' +
				", comment2='" + comment2 + '\'' +
				", currencyCode='" + currencyCode + '\'' +
				", inputIpAddress='" + inputIpAddress + '\'' +
				", invoice='" + invoice + '\'' +
				'}';
	}

	/**
	 * Performs JSR 303 validation on the object.
	 *
	 * @return constraint violations
	 */
	public Set<ConstraintViolation<ProtectPayPayment>> validate() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		return validator.validate(this, getClass());
	}
}
