package com.netradius.payvision.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Abhinav Nahar
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "nmi_response")
public class PayVisionQueryResponse implements Serializable {
	@JacksonXmlElementWrapper(useWrapping = false)
	@JacksonXmlProperty(localName = "transaction")
	private  PayVisionTransactionResponse transaction;

	@Data
	public static class PayVisionTransactionResponse {
		@JacksonXmlProperty(localName = "transaction_id")
		private String transactionId;
		@JacksonXmlProperty(localName = "partial_payment_id")
		private String partialPaymentId;
		@JacksonXmlProperty(localName = "partial_payment_balance")
		private String partialPaymentBalance;
		@JacksonXmlProperty(localName = "platform_id")
		private String platformId;
		@JacksonXmlProperty(localName = "transaction_type")
		private String transactionType;
		private String condition;
		@JacksonXmlProperty(localName = "order_id")
		private String orderId;
		@JacksonXmlProperty(localName = "authorization_code")
		private String authorizationCode;
		@JacksonXmlProperty(localName = "ponumber")
		private String purchaseOrderNumber;
		@JacksonXmlProperty(localName = "order_description")
		private String orderDescription;
		@JacksonXmlProperty(localName = "first_name")
		private String firstName;
		@JacksonXmlProperty(localName = "last_name")
		private String lastName;
		@JacksonXmlProperty(localName = "address_1")
		private String address1;
		@JacksonXmlProperty(localName = "address_2")
		private String address2;
		private String company;
		private String city;
		private String state;
		@JacksonXmlProperty(localName = "postal_code")
		private String postalCode;
		private String country;
		private String email;
		private String phone;
		private String fax;
		@JacksonXmlProperty(localName = "cell_phone")
		private String cellPhone;
		@JacksonXmlProperty(localName = "customertaxid")
		private String customertaxid;
		@JacksonXmlProperty(localName = "customerid")
		private String customerid;
		private String website;
		@JacksonXmlProperty(localName = "shipping_first_name")
		private String shippingFirstName;
		@JacksonXmlProperty(localName = "shipping_last_name")
		private String shippingLastName;
		@JacksonXmlProperty(localName = "shipping_address_1")
		private String shippingAddress1;
		@JacksonXmlProperty(localName = "shipping_address_2")
		private String shippingAddress2;
		@JacksonXmlProperty(localName = "shipping_company")
		private String shippingCompany;
		@JacksonXmlProperty(localName = "shipping_city")
		private String shippingCity;
		@JacksonXmlProperty(localName = "shipping_state")
		private String shippingState;
		@JacksonXmlProperty(localName = "shipping_postal_code")
		private String shippingPostalCode;
		@JacksonXmlProperty(localName = "shipping_country")
		private String shippingCountry;
		@JacksonXmlProperty(localName = "shipping_email")
		private String shippingEmail;
		@JacksonXmlProperty(localName = "shipping_carrier")
		private String shippingCarrier;
		@JacksonXmlProperty(localName = "tracking_number")
		private String trackingNumber;
		@JacksonXmlProperty(localName = "shipping_date")
		private String shippingDate;
		@JacksonXmlProperty(localName = "shipping")
		private String shippingAmount;
		@JacksonXmlProperty(localName = "shipping_phone")
		private String shippingPhone;
		@JacksonXmlProperty(localName = "cc_number")
		private String ccNumber;
		@JacksonXmlProperty(localName = "cc_hash")
		private String ccHash;
		@JacksonXmlProperty(localName = "cc_exp")
		private String ccExp;
		@JacksonXmlProperty(localName = "cavv")
		private String cavv;
		@JacksonXmlProperty(localName = "cavv_result")
		private String cavvResult;
		@JacksonXmlProperty(localName = "xid")
		private String xid;
		@JacksonXmlProperty(localName = "avs_response")
		private String avsResponse;
		@JacksonXmlProperty(localName = "csc_response")
		private String cscResponse;
		@JacksonXmlProperty(localName = "cardholder_auth")
		private String cardholderAuth;
		@JacksonXmlProperty(localName = "cc_start_date")
		private String ccStartDate;
		@JacksonXmlProperty(localName = "cc_issue_number")
		private String ccIssueNumber;
		@JacksonXmlProperty(localName = "check_account")
		private String checkAccount;
		@JacksonXmlProperty(localName = "check_hash")
		private String checkHash;
		@JacksonXmlProperty(localName = "check_aba")
		private String checkAba;
		@JacksonXmlProperty(localName = "check_name")
		private String checkName;
		@JacksonXmlProperty(localName = "account_holder_type")
		private String accountHolderType;
		@JacksonXmlProperty(localName = "account_type")
		private String accountType;
		@JacksonXmlProperty(localName = "sec_code")
		private String secCode;
		@JacksonXmlProperty(localName = "drivers_license_number")
		private String driversLicenseNumber;
		@JacksonXmlProperty(localName = "drivers_license_state")
		private String driversLicenseState;
		@JacksonXmlProperty(localName = "drivers_license_dob")
		private String driversLicenseDob;
		@JacksonXmlProperty(localName = "social_security_number")
		private String socialSecurityNumber;
		@JacksonXmlProperty(localName = "processor_id")
		private String processorId;
		private String tax;
		private String currency;
		private String surcharge;
		private String tip;
		@JacksonXmlProperty(localName = "card_balance")
		private String cardBalance;
		@JacksonXmlProperty(localName = "card_available_balance")
		private String cardAvailableBalance;
		@JacksonXmlProperty(localName = "entry_mode")
		private String entryMode;
		@JacksonXmlProperty(localName = "cc_bin")
		private String ccBin;
		@JacksonXmlElementWrapper(useWrapping = false)
		@JacksonXmlProperty(localName = "action")
		private Action[] action;

		@Data
		public static class Action {

			public void Action() {}
			private BigDecimal amount;
			@JacksonXmlProperty(localName = "action_type")
			private String action_type;
			private String date;
			private String success;
			@JacksonXmlProperty(localName = "ip_address")
			private String ip_address;
			private String source;
			private String username;
			@JacksonXmlProperty(localName = "response_text")
			private String response_text;
			@JacksonXmlProperty(localName = "batch_id")
			private String batch_id;
			@JacksonXmlProperty(localName = "processor_batch_id")
			private String processor_batch_id;
			@JacksonXmlProperty(localName = "response_code")
			private String response_code;
			@JacksonXmlProperty(localName = "processor_response_text")
			private String processor_response_text;
			@JacksonXmlProperty(localName = "processor_response_code")
			private String processor_response_code;
			@JacksonXmlProperty(localName = "device_license_number")
			private String device_license_number;
			@JacksonXmlProperty(localName = "device_nickname")
			private String device_nickname;
		}

	}


}
