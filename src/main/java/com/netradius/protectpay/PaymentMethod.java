package com.netradius.protectpay;

import javax.validation.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * Holds payment method details.
 *
 * @author Erik R. Jensen
 */
public class PaymentMethod implements Serializable {

	private static final long serialVersionUID = -5673867769460368617L;

	public enum AccountCountryCode {
		ISO_820("820"),
		ISO_124("124");

		private String value;

		private AccountCountryCode(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	public enum DuplicateAction {
		SAVENEW,
		ERROR,
		RETURNDUP
	}

	public enum Type {
		Visa,
		MasterCard,
		AMEX,
		Discover,
		DinersClub,
		JCB,
		ProPayToProPay,
		Checking,
		Savings
	}

	@Size(max = 36)
	private String paymentMethodId;

	private AccountCountryCode accountCountryCode;

	@Size(max = 50)
	private String accountName;

	@Size(max = 25)
	private String accountNumber;

	@Size(max = 50)
	private String bankNumber;

	@Size(max = 50)
	private String description;

	private DuplicateAction duplicateAction;

	@Size(max = 4)
	private String expirationDate;

	@Size(max = 16)
	private String payerAccountId;

	private Type type;

	private Boolean payerProtected;

	private Integer priority;

	@Valid
	private BillingInfo billing;

	private Date dateCreated;

	/**
	 * Returns the payment method ID.
	 *
	 * @return the payment method ID
	 */
	public String getPaymentMethodId() {
		return paymentMethodId;
	}

	/**
	 * Sets the payment method ID. The max length is 36 characters.
	 *
	 * @param paymentMethodId the payment method ID
	 */
	public void setPaymentMethodId(String paymentMethodId) {
		this.paymentMethodId = paymentMethodId;
	}

	/**
	 * Returns the account country code.
	 *
	 * @return the account country code
	 */
	public AccountCountryCode getAccountCountryCode() {
		return accountCountryCode;
	}

	/**
	 * Sets the account country code.
	 *
	 * @param accountCountryCode the account country code
	 */
	public void setAccountCountryCode(AccountCountryCode accountCountryCode) {
		this.accountCountryCode = accountCountryCode;
	}

	/**
	 * Returns the account name.
	 *
	 * @return the account name
	 */
	public String getAccountName() {
		return accountName;
	}

	/**
	 * Sets the account name. The max length is 50 characters.
	 *
	 * @param accountName the account name
	 */
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	/**
	 * Returns the account number.
	 *
	 * @return the account number
	 */
	public String getAccountNumber() {
		return accountNumber;
	}

	/**
	 * Sets the account number. The max length is 25 characters. For credit cards, this is the number
	 * on the card.
	 *
	 * @param accountNumber the account number
	 */
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	/**
	 * Returns the bank number. This is the routing number for bank accounts.
	 *
	 * @return the bank account number
	 */
	public String getBankNumber() {
		return bankNumber;
	}

	/**
	 * Sets the bank number. This is the routing number for bank accounts.
	 *
	 * @param bankNumber the bank account number
	 */
	public void setBankNumber(String bankNumber) {
		this.bankNumber = bankNumber;
	}

	/**
	 * Returns the description of the payment method.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description of the payment method. The max length is 50 characters.
	 *
	 * @param description the description of the payment method
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns the duplicate action. This is the action to take if a duplicate payment method is found.
	 *
	 * @return the duplicate action
	 */
	public DuplicateAction getDuplicateAction() {
		return duplicateAction;
	}

	/**
	 * Sets the duplicate action. This is the action to take if a duplicate payment method is found.
	 *
	 * @param duplicateAction the duplicate action
	 */
	public void setDuplicateAction(DuplicateAction duplicateAction) {
		this.duplicateAction = duplicateAction;
	}

	/**
	 * Returns the expiration date. This is the MMYY value.
	 *
	 * @return the expiration date
	 */
	public String getExpirationDate() {
		return expirationDate;
	}

	/**
	 * Sets the expiration date. This is the MMYY value. The max length is 4 digits.
	 *
	 * @param expirationDate the expiration date
	 */
	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}

	/**
	 * Returns the payer account ID.
	 *
	 * @return the payer account ID
	 */
	public String getPayerAccountId() {
		return payerAccountId;
	}

	/**
	 * Sets the payer account ID. The max length is 16.
	 *
	 * @param payerAccountId the payer account ID
	 */
	public void setPayerAccountId(String payerAccountId) {
		this.payerAccountId = payerAccountId;
	}

	/**
	 * Returns the payment type.
	 *
	 * @return the payment type
	 */
	public Type getType() {
		return type;
	}

	/**
	 * Sets the payment type.
	 *
	 * @param type the payment type
	 */
	public void setType(Type type) {
		this.type = type;
	}

	/**
	 * Returns the payer protected flag.
	 *
	 * @return the payer protected flag
	 */
	public Boolean getPayerProtected() {
		return payerProtected;
	}

	/**
	 * Sets the payer protected flag.
	 *
	 * @param payerProtected the payer protected flag
	 */
	public void setPayerProtected(Boolean payerProtected) {
		this.payerProtected = payerProtected;
	}

	/**
	 * Returns the priority.
	 *
	 * @return the priority
	 */
	public Integer getPriority() {
		return priority;
	}

	/**
	 * Sets the priority.
	 *
	 * @param priority the priority
	 */
	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	/**
	 * Returns the billing information.
	 *
	 * @return the billing information
	 */
	public BillingInfo getBilling() {
		return billing;
	}

	/**
	 * Sets the billing information.
	 *
	 * @param billing the billing information
	 */
	public void setBilling(BillingInfo billing) {
		this.billing = billing;
	}

	/**
	 * Returns the created date.
	 *
	 * @return the created date
	 */
	public Date getDateCreated() {
		return dateCreated;
	}

	/**
	 * Sets the created date. This value is set by ProtectPay when a payment method is created.
	 *
	 * @param dateCreated the created date
	 */
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	@Override
	public String toString() {
		return "PaymentMethod{" +
				"paymentMethodId='" + paymentMethodId + '\'' +
				", accountCountryCode=" + accountCountryCode +
				", accountName='" + accountName + '\'' +
				", accountNumber='" + accountNumber + '\'' +
				", bankNumber='" + bankNumber + '\'' +
				", description='" + description + '\'' +
				", duplicateAction=" + duplicateAction +
				", expirationDate='" + expirationDate + '\'' +
				", payerAccountId='" + payerAccountId + '\'' +
				", type=" + type +
				", payerProtected=" + payerProtected +
				", priority=" + priority +
				", billing=" + billing +
				", dateCreated=" + dateCreated +
				'}';
	}

	/**
	 * Performs JSR 303 validation on the object.
	 *
	 * @return constraint violations
	 */
	public Set<ConstraintViolation<PaymentMethod>> validate() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		return validator.validate(this, getClass());
	}
}
