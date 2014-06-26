package com.netradius.protectpay;

import com.propay.sps.SPS;
import com.propay.sps.SPSService;
import com.propay.sps.types.*;
import com.propay.sps.types.ObjectFactory;
import org.datacontract.schemas._2004._07.propay_contracts_sps.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Wrapper class for the ProtectPay API.
 *
 * @author Erik R. Jensen
 */
public class ProtectPayClient {

	public static final String PRODUCTION_URL = "http://protectpay.propay.com/API/SPS.svc?wsdl";
	public static final String TESTING_URL = "http://protectpaytest.propay.com/API/SPS.svc?wsdl";

	private static final Logger log = LoggerFactory.getLogger(ProtectPayClient.class);

	private SPSService service;
	private ObjectFactory typesFactory;
	private org.datacontract.schemas._2004._07.propay_contracts_sps.ObjectFactory contractsFactory;
	private ID id;

	private enum Type {
		AUTH,
		CAPTURE,
		CREDIT
	}

	/**
	 * Creates a new ProtectPayClient instance.
	 *
	 * @param wsdlUrl the WSDL URL to use
	 * @param authenticationToken the authentication token assigned by ProPay
	 * @param billerAccountId the biller account ID assigned by ProPay
	 */
	public ProtectPayClient(String wsdlUrl, String authenticationToken, String billerAccountId) {
		try {
			SPS sps = new SPS(new URL(wsdlUrl));
			service = sps.getBasicHttpBindingSPSService();
		} catch (MalformedURLException x) {
			throw new IllegalArgumentException("Invalid URL [" + wsdlUrl + "]: " + x.getMessage(), x);
		}
		typesFactory = new ObjectFactory();
		contractsFactory = new org.datacontract.schemas._2004._07.propay_contracts_sps.ObjectFactory();
		id = typesFactory.createID();
		id.setAuthenticationToken(typesFactory.createIDAuthenticationToken(authenticationToken));
		id.setBillerAccountId(typesFactory.createIDBillerAccountId(billerAccountId));
	}

	private void checkResult(Result result) throws ProtectPayException {
		if (!"00".equals(result.getResultCode().getValue())) {
			throw new ProtectPayException(
					result.getResultCode().getValue(),
					result.getResultValue().getValue(),
					result.getResultMessage().getValue()
			);
		}
	}

	private PayerData toPayerData(Payer payer) {
		PayerData data = typesFactory.createPayerData();
		data.setName(typesFactory.createPayerDataName(payer.getAccountName()));
		data.setEmailAddress(typesFactory.createPayerDataEmailAddress(payer.getEmailAddress()));
		data.setExternalId1(typesFactory.createPayerDataExternalId1(payer.getExternalId1()));
		data.setExternalId2(typesFactory.createPayerDataExternalId2(payer.getExternalId2()));
		return data;
	}

	private Billing toBilling(BillingInfo info) {
		Billing billing = new Billing();
		billing.setAddress1(typesFactory.createBillingAddress1(info.getAddress1()));
		billing.setAddress2(typesFactory.createBillingAddress2(info.getAddress2()));
		billing.setAddress3(typesFactory.createBillingAddress3(info.getAddress3()));
		billing.setCity(typesFactory.createBillingCity(info.getCity()));
		billing.setState(typesFactory.createBillingState(info.getState()));
		billing.setZipCode(typesFactory.createBillingZipCode(info.getZipCode()));
		billing.setCountry(typesFactory.createBillingCountry(info.getCountry().toString()));
		billing.setEmail(typesFactory.createBillingEmail(info.getEmailAddress()));
		billing.setTelephoneNumber(typesFactory.createBillingTelephoneNumber(info.getTelephoneNumber()));
		return billing;
	}

	private BillingInfo toBillingInfo(Billing billing) {
		BillingInfo info = new BillingInfo();
		info.setAddress1(billing.getAddress1().getValue());
		info.setAddress2(billing.getAddress2().getValue());
		info.setAddress3(billing.getAddress3().getValue());
		info.setEmailAddress(billing.getEmail().getValue());
		info.setCity(billing.getCity().getValue());
		info.setState(billing.getState().getValue());
		info.setCountry(BillingInfo.Country.valueOf(billing.getCountry().getValue()));
		info.setTelephoneNumber(billing.getTelephoneNumber().getValue());
		info.setZipCode(billing.getZipCode().getValue());
		return info;
	}

	private PaymentMethod toPaymentMethod(PaymentMethodInformation info) {
		PaymentMethod paymentMethod = new PaymentMethod();
		paymentMethod.setAccountName(info.getAccountName().getValue());
		paymentMethod.setDateCreated(info.getDateCreated().toGregorianCalendar().getTime());
		paymentMethod.setDescription(info.getDescription().getValue());
		paymentMethod.setExpirationDate(info.getExpirationDate().getValue());
		paymentMethod.setAccountNumber(info.getObfuscatedAccountNumber().getValue());
		paymentMethod.setPaymentMethodId(info.getPaymentMethodID().getValue());
		paymentMethod.setType(PaymentMethod.Type.valueOf(info.getPaymentMethodType().getValue()));
		paymentMethod.setPriority(info.getPriority());
		Billing billing = info.getBillingInformation().getValue();
		if (billing != null) {
			paymentMethod.setBilling(toBillingInfo(billing));
		}
		return paymentMethod;
	}

	private Transaction toTransaction(Payment payment) {
		Transaction transaction = new Transaction();
		transaction.setPayerAccountId(typesFactory.createTransactionPayerAccountId(payment.getPayerAccountId()));
		transaction.setAmount(typesFactory.createTransactionAmount(payment.getAmount().toString()));
		transaction.setComment1(typesFactory.createTransactionComment1(payment.getComment1()));
		transaction.setComment2(typesFactory.createTransactionComment2(payment.getComment2()));
		transaction.setCurrencyCode(typesFactory.createTransactionCurrencyCode(payment.getCurrencyCode()));
		transaction.setInputIpAddress(typesFactory.createTransactionInputIpAddress(payment.getInputIpAddress()));
		transaction.setInvoice(typesFactory.createTransactionInvoice(payment.getInvoice()));
		if (payment.getMerchantProfileId() != null) {
			transaction.setMerchantProfileId(
					typesFactory.createTransactionMerchantProfileId(payment.getMerchantProfileId().toString()));
		}
		return transaction;
	}

	private CreditCardOverrides toCreditCardOverrides(CreditCardOverride cco) {
		CreditCardOverrides overrides = new CreditCardOverrides();
		if (cco.getFullName() != null) {
			overrides.setFullName(contractsFactory.createCreditCardOverridesFullName(cco.getFullName()));
		}
		if (cco.getCvv() != null) {
			overrides.setCVV(contractsFactory.createCreditCardOverridesCVV(cco.getCvv()));
		}
		if (cco.getExpiration() != null) {
			overrides.setExpirationDate(
					contractsFactory.createCreditCardOverridesExpirationDate(cco.getExpiration()));
		}
		if (cco.getBilling() != null) {
			overrides.setBilling(contractsFactory.createCreditCardOverridesBilling(toBilling(cco.getBilling())));
		}
		return overrides;
	}

	private AchOverrides toAchOverrides(ACHOverride ao) {
		AchOverrides overrides = new AchOverrides();
		if (ao.getBankAccountType() != null) {
			overrides.setBankAccountType(
					contractsFactory.createAchOverridesBankAccountType(ao.getBankAccountType().toString()));
		}
		if (ao.getSecCode() != null) {
			overrides.setSecCode(contractsFactory.createAchOverridesSecCode(ao.getSecCode().name()));
		}
		return overrides;
	}

	private PaymentResponse toPaymentResponse(TransactionInformation info) {
		PaymentResponse paymentResponse = new PaymentResponse();
		paymentResponse.setAuthorizationCode(info.getAuthorizationCode().getValue());
		paymentResponse.setAvsCode(info.getAVSCode().getValue());
		paymentResponse.setConversionRate(info.getCurrencyConversionRate());
		paymentResponse.setConvertedAmount(info.getCurrencyConvertedAmount());
		paymentResponse.setConvertedCurrencyCode(info.getCurrencyConvertedCurrencyCode().getValue());
		Result result = info.getResultCode().getValue();
		if (result != null) {
			paymentResponse.setResultCode(result.getResultCode().getValue());
			paymentResponse.setResultMessage(result.getResultMessage().getValue());
			paymentResponse.setResultValue(result.getResultValue().getValue());
		}
		if (info.getTransactionHistoryId() != null) {
			paymentResponse.setTransactionHistoryId(Long.parseLong(info.getTransactionHistoryId().getValue()));
		}
		paymentResponse.setTransactionId(info.getTransactionId().getValue());
		paymentResponse.setTransactionResult(info.getTransactionResult().getValue());
		return paymentResponse;
	}

	/**
	 * Creates a new payer.
	 *
	 * @param accountName the account name used to identify this payer
	 * @return the payer account ID generated by ProtectPay
	 *
	 * @throws ProtectPayException if the request fails
	 */
	public String createPayer(String accountName) throws ProtectPayException {
		CreateAccountInformationResult response = service.createPayer(id, accountName);
		Result result = response.getRequestResult().getValue();
		checkResult(result);
		return response.getExternalAccountID().getValue();
	}

	/**
	 * Creates a new payer. This method will return the generated payer account ID as well
	 * as set the value on the Payer argument.
	 *
	 * @param payer the payer to create
	 * @return the payer account ID generated by ProtectPay
	 *
	 * @throws ProtectPayException if the request fails
	 */
	public String createPayer(Payer payer) throws ProtectPayException {
		CreateAccountInformationResult response = service.createPayerWithData(id, toPayerData(payer));
		Result result = response.getRequestResult().getValue();
		checkResult(result);
		String accountId = response.getExternalAccountID().getValue();
		payer.setPayerAccountId(accountId);
		return accountId;
	}

	/**
	 * Updates an existing payer.
	 *
	 * @param payer the payer to update with the data
	 *
	 * @throws ProtectPayException if the request fails
	 */
	public void updatePayer(Payer payer) throws ProtectPayException {
		EditPayerRequest request = new EditPayerRequest();
		request.setPayerAccountId(contractsFactory.createEditPayerRequestPayerAccountId(payer.getPayerAccountId()));
		request.setUpdatedData(contractsFactory.createEditPayerRequestUpdatedData(toPayerData(payer)));
		Result result = service.editPayerV2(id, request);
		checkResult(result);
	}

	/**
	 * Deletes a payer.
	 *
	 * @param payerAccountId the payer account ID to delete
	 *
	 * @throws ProtectPayException if the request fails
	 */
	public void deletePayer(String payerAccountId) throws ProtectPayException {
		Result result = service.deletePayer(id, payerAccountId);
		checkResult(result);
	}

	/**
	 * Searches for payers.
	 *
	 * @param payer the data to use as a criteria
	 * @return the matching payers
	 *
	 * @throws ProtectPayException if the request fails
	 */
	public List<Payer> getPayers(Payer payer) throws ProtectPayException {
		GetPayersResult response = service.getPayers(id, payer == null ? null : toPayerData(payer));
		Result result = response.getRequestResult().getValue();
		checkResult(result);
		ArrayOfPayerInfo aopi = response.getPayers().getValue();
		if (aopi != null) {
			List<Payer> payers = new ArrayList<>(aopi.getPayerInfo().size());
			for (PayerInfo info : aopi.getPayerInfo()) {
				Payer foundPayer = new Payer();
				foundPayer.setPayerAccountId(info.getPayerAccountId().getValue());
				foundPayer.setAccountName(info.getName().getValue());
				foundPayer.setExternalId1(info.getExternalId1().getValue());
				foundPayer.setExternalId2(info.getExternalId2().getValue());
				payers.add(foundPayer);
			}
			return payers;
		}
		return new ArrayList<>(0);
	}

	/**
	 * Creates a new payment method. This method will return the generated payment method ID
	 * as well as set the value on the PaymentMethod argument.
	 *
	 * @param paymentMethod the payment method to create
	 * @return the generated payment method ID
	 *
	 * @throws ProtectPayException if the request fails
	 */
	public String createPaymentMethod(PaymentMethod paymentMethod) throws ProtectPayException {
		PaymentMethodAdd request = new PaymentMethodAdd();
		if (paymentMethod.getAccountCountryCode() != null) {
			request.setAccountCountryCode(typesFactory.createPaymentMethodAddAccountCountryCode(
					paymentMethod.getAccountCountryCode().getValue()));
		}
		request.setAccountName(typesFactory.createPaymentMethodAddAccountName(paymentMethod.getAccountName()));
		request.setAccountNumber(typesFactory.createPaymentMethodAddAccountNumber(paymentMethod.getAccountNumber()));
		request.setBankNumber(typesFactory.createPaymentMethodAddBankNumber(paymentMethod.getBankNumber()));
		request.setDescription(typesFactory.createPaymentMethodAddDescription(paymentMethod.getDescription()));
		if (paymentMethod.getDuplicateAction() != null) {
			request.setDuplicateAction(typesFactory.createPaymentMethodAddDuplicateAction(
					paymentMethod.getDuplicateAction().toString()));
		}
		request.setExpirationDate(typesFactory.createPaymentMethodAddExpirationDate(paymentMethod.getExpirationDate()));
		request.setPayerAccountId(typesFactory.createPaymentMethodAddPayerAccountId(paymentMethod.getPayerAccountId()));
		if (paymentMethod.getType() != null) {
			request.setPaymentMethodType(typesFactory.createPaymentMethodAddPaymentMethodType(
					paymentMethod.getType().toString()));
		}
		if (paymentMethod.getPayerProtected() != null) {
			request.setProtected(paymentMethod.getPayerProtected());
		}
		request.setPriority(paymentMethod.getPriority());
		if (paymentMethod.getBilling() != null) {
			request.setBillingInformation(typesFactory.createBilling(toBilling(paymentMethod.getBilling())));
		}

		CreatePaymentMethodResult response = service.createPaymentMethod(id, request);
		Result result = response.getRequestResult().getValue();
		checkResult(result);
		String paymentMethodId = response.getPaymentMethodId().getValue();
		paymentMethod.setPaymentMethodId(paymentMethodId);
		return paymentMethodId;
	}

	/**
	 * Updated a payment method. While this method accepts a payment method as an argument, not all the fields
	 * are available to be updated. The following fields are not available for updates: accountCountryCode,
	 * accountNumber, bankNumber and type (if credit card).
	 *
	 * @param paymentMethod the payment method to update
	 *
	 * @throws ProtectPayException if the request fails
	 */
	public void updatePaymentMethod(PaymentMethod paymentMethod) throws ProtectPayException {
		PaymentMethodUpdate request = new PaymentMethodUpdate();
		request.setPayerAccountId(typesFactory.createPaymentMethodUpdatePayerAccountId(paymentMethod.getPayerAccountId()));
		request.setAccountName(typesFactory.createPaymentMethodUpdateAccountName(paymentMethod.getAccountName()));
		if (paymentMethod.getType() == PaymentMethod.Type.Checking || paymentMethod.getType() == PaymentMethod.Type.Savings) {
			request.setBankAccountType(typesFactory.createPaymentMethodUpdateBankAccountType(paymentMethod.getType().toString()));
		}
		request.setDescription(typesFactory.createPaymentMethodUpdateDescription(paymentMethod.getDescription()));
		request.setExpirationDate(typesFactory.createPaymentMethodUpdateExpirationDate(paymentMethod.getExpirationDate()));
		request.setPaymentMethodID(typesFactory.createPaymentMethodUpdatePaymentMethodID(paymentMethod.getPaymentMethodId()));
		if (paymentMethod.getPayerProtected() != null) {
			request.setProtected(typesFactory.createPaymentMethodUpdateProtected(paymentMethod.getPayerProtected()));
		}
		if (paymentMethod.getBilling() != null) {
			request.setBillingInformation(typesFactory.createPaymentMethodUpdateBillingInformation(
					toBilling(paymentMethod.getBilling())));
		}
		Result result = service.editPaymentMethod(id, request);
		checkResult(result);
	}

	/**
	 * Deletes a payment method.
	 *
	 * @param payerAccountId the payer account ID the payment method belongs to
	 * @param paymentMethodId the payment method ID to delete
	 *
	 * @throws ProtectPayException if the request fails
	 */
	public void deletePaymentMethod(String payerAccountId, String paymentMethodId) throws ProtectPayException {
		Result result = service.deletePaymentMethod(id, payerAccountId, paymentMethodId);
		checkResult(result);
	}

	/**
	 * Finds all the payment methods associated with a payer account.
	 *
	 * @param payerAccountId the payer account ID
	 * @return all payment methods associated with the payer account
	 *
	 * @throws ProtectPayException if the request fails
	 */
	public List<PaymentMethod> getPaymentMethods(String payerAccountId) throws ProtectPayException {
		PaymentMethodsResult response = service.getAllPayerPaymentMethods(id, payerAccountId);
		Result result = response.getRequestResult().getValue();
		checkResult(result);
		ArrayOfPaymentMethodInformation aopmi = response.getPaymentMethods().getValue();
		if (aopmi != null) {
			List<PaymentMethod> paymentMethods = new ArrayList<>(aopmi.getPaymentMethodInformation().size());
			for (PaymentMethodInformation info : aopmi.getPaymentMethodInformation()) {
				paymentMethods.add(toPaymentMethod(info));
			}
			return paymentMethods;
		} else {
			return new ArrayList<>(0);
		}
	}

	/**
	 * Finds a payment method.
	 *
	 * @param payerAccountId the payer account ID
	 * @param paymentMethodId the payment method ID
	 * @return the found payment method or null
	 *
	 * @throws ProtectPayException if the request fails
	 */
	public PaymentMethod getPaymentMethod(String payerAccountId, String paymentMethodId)
			throws ProtectPayException {
		PaymentMethodsResult response = service.getPayerPaymentMethod(id, payerAccountId, paymentMethodId);
		Result result = response.getRequestResult().getValue();
		checkResult(result);
		ArrayOfPaymentMethodInformation aopmi = response.getPaymentMethods().getValue();
		if (aopmi != null) {
			if (aopmi.getPaymentMethodInformation().size() > 0) {
				return toPaymentMethod(aopmi.getPaymentMethodInformation().get(0));
			}
		}
		return null;
	}

	private PaymentResponse transact(Payment payment, CreditCardOverride cco, ACHOverride ao,
			boolean recurring, Type type) throws ProtectPayException {
		Transaction transaction = toTransaction(payment);
		PaymentInfoOverrides paymentInfoOverrides = new PaymentInfoOverrides();
		if (cco != null) {
			paymentInfoOverrides.setCreditCard(
					contractsFactory.createPaymentInfoOverridesCreditCard(toCreditCardOverrides(cco)));
		}
		if (ao != null) {
			paymentInfoOverrides.setAch(contractsFactory.createPaymentInfoOverridesAch(toAchOverrides(ao)));
		}
		TransactionResult response = null;
		switch (type) {
			case AUTH:
				if (recurring) {
					response = service.authorizePaymentMethodTransactionRecurring(
							id, transaction, payment.getPaymentMethodId(), paymentInfoOverrides);
				} else {
					response = service.authorizePaymentMethodTransaction(
							id, transaction, payment.getPaymentMethodId(), paymentInfoOverrides);
				}
				break;
			case CAPTURE:
				if (recurring) {
					response = service.processPaymentMethodTransactionRecurring(
							id, transaction, payment.getPaymentMethodId(), paymentInfoOverrides);
				} else {
					response = service.processPaymentMethodTransaction(
							id, transaction, payment.getPaymentMethodId(), paymentInfoOverrides);
				}
				break;
			case CREDIT:
				response = service.creditPayment(
						id, transaction, payment.getPaymentMethodId(), paymentInfoOverrides);
				break;
		}
		Result result = response.getRequestResult().getValue();
		checkResult(result);
		TransactionInformation info = response.getTransaction().getValue();
		return toPaymentResponse(info);
	}

	/**
	 * Executes an auth transaction.
	 *
	 * @param payment the payment data
	 * @param override any override values
	 * @param recurring true if recurring, false if otherwise
	 * @return the payment response
	 * @throws ProtectPayException if an error is returned from ProtectPay
	 */
	public PaymentResponse auth(Payment payment, CreditCardOverride override, boolean recurring) throws ProtectPayException {
		return transact(payment, override, null, recurring, Type.AUTH);
	}

	/**
	 * Executes an auth transaction.
	 *
	 * @param payment the payment data
	 * @param cvv the CVV value
	 * @param recurring true if recurring, false if otherwise
	 * @return the payment response
	 * @throws ProtectPayException if an error is returned from ProtectPay
	 */
	public PaymentResponse auth(Payment payment, String cvv, boolean recurring) throws ProtectPayException {
		CreditCardOverride cco = new CreditCardOverride();
		cco.setCvv(cvv);
		return transact(payment, cco, null, recurring, Type.AUTH);
	}

	/**
	 * Executes an auth transaction.
	 *
	 * @param payment the payment data
	 * @param override any override values
	 * @param recurring true if recurring, false if otherwise
	 * @return the payment response
	 * @throws ProtectPayException if an error is returned from ProtectPay
	 */
	public PaymentResponse auth(Payment payment, ACHOverride override, boolean recurring) throws ProtectPayException {
		return transact(payment, null, override, recurring, Type.AUTH);
	}

	/**
	 * Executes an auth transaction.
	 *
	 * @param payment the payment data
	 * @param recurring true if recurring, false if otherwise
	 * @return the payment response
	 * @throws ProtectPayException if an error is returned from ProtectPay
	 */
	public PaymentResponse auth(Payment payment, boolean recurring) throws ProtectPayException {
		return transact(payment, null, null, recurring, Type.AUTH);
	}

	/**
	 * Executes a capture transaction.
	 *
	 * @param payment the prior payment data
	 * @param amount the amount to capture
	 * @return the payment response
	 * @throws ProtectPayException if an error is returned from ProtectPay
	 */
	public PaymentResponse capture(PriorPayment payment, int amount)
			throws ProtectPayException {
		CaptureRequest request = new CaptureRequest();
		if (payment.getMerchantProfileId() != null) {
			request.setMerchantProfileId(
					contractsFactory.createCaptureRequestMerchantProfileId(payment.getMerchantProfileId().toString()));
		}
		request.setComment1(contractsFactory.createCaptureRequestComment1(payment.getComment1()));
		request.setComment2(contractsFactory.createCaptureRequestComment2(payment.getComment2()));
		request.setAmount(amount);
		request.setOriginalTransactionId(
				contractsFactory.createCaptureRequestOriginalTransactionId(payment.getOriginalTransactionId()));
		request.setTransactionHistoryId(payment.getTransactionHistoryId());
		TransactionResult response = service.capturePaymentV2(id, request);
		Result result = response.getRequestResult().getValue();
		checkResult(result);
		TransactionInformation info = response.getTransaction().getValue();
		return toPaymentResponse(info);
	}

	private PaymentResponse authAndCapture(Payment payment, CreditCardOverride cco, ACHOverride ao, boolean recurring)
			throws ProtectPayException {
		return transact(payment, cco, ao, recurring, Type.CAPTURE);
	}

	/**
	 * Executes an auth and capture request.
	 *
	 * @param payment the payment data
	 * @param cco any override values
	 * @param recurring true if recurring, false if otherwise
	 * @return the payment response
	 * @throws ProtectPayException if an error is returned from ProtectPay
	 */
	public PaymentResponse authAndCapture(Payment payment, CreditCardOverride cco, boolean recurring)
			throws ProtectPayException {
		return transact(payment, cco, null, recurring, Type.CAPTURE);
	}

	/**
	 * Executes and auth and capture transaction.
	 *
	 * @param payment the payment data
	 * @param cvv the CVV value
	 * @param recurring true if recurring, false if otherwise
	 * @return the payment response
	 * @throws ProtectPayException if an error is returned from ProtectPay
	 */
	public PaymentResponse authAndCapture(Payment payment, String cvv, boolean recurring)
			throws ProtectPayException {
		CreditCardOverride cco = new CreditCardOverride();
		cco.setCvv(cvv);
		return transact(payment, cco, null, recurring, Type.CAPTURE);
	}

	/**
	 * Executes an auth and capture transaction.
	 *
	 * @param payment the payment data
	 * @param ao any override values
	 * @param recurring true if recurring, false if otherwise
	 * @return the payment response
	 * @throws ProtectPayException if an error is returned from ProtectPay
	 */
	public PaymentResponse authAndCapture(Payment payment, ACHOverride ao, boolean recurring)
			throws ProtectPayException {
		return transact(payment, null, ao, recurring, Type.CAPTURE);
	}

	/**
	 * Executes an auth and capture transaction.
	 *
	 * @param payment the payment data
	 * @param recurring true if recurring, false if otherwise
	 * @return the payment response
	 * @throws ProtectPayException if an error is returned from ProtectPay
	 */
	public PaymentResponse authAndCapture(Payment payment, boolean recurring)
			throws ProtectPayException {
		return transact(payment, null, null, recurring, Type.CAPTURE);
	}

	/**
	 * Voids a previous transaction. This generally only works against transaction that have gone through auth,
	 * but not capture.
	 *
	 * @param priorPayment the prior payment data
	 * @return the payment response
	 * @throws ProtectPayException if an error is returned from ProtectPay
	 */
	public PaymentResponse voidPayment(PriorPayment priorPayment) throws ProtectPayException {
		VoidRequest request = new VoidRequest();
		request.setOriginalTransactionId(
				contractsFactory.createVoidRequestOriginalTransactionId(priorPayment.getOriginalTransactionId()));
		request.setTransactionHistoryId(priorPayment.getTransactionHistoryId());
		request.setComment1(
				contractsFactory.createVoidRequestComment1(priorPayment.getComment1()));
		request.setComment2(
				contractsFactory.createVoidRequestComment2(priorPayment.getComment2()));
		if (priorPayment.getMerchantProfileId() != null) {
			request.setMerchantProfileId(
					contractsFactory.createVoidRequestMerchantProfileId
							(priorPayment.getMerchantProfileId().toString()));
		}
		TransactionResult response = service.voidPaymentV2(id, request);
		Result result = response.getRequestResult().getValue();
		checkResult(result);
		TransactionInformation info = response.getTransaction().getValue();
		return toPaymentResponse(info);
	}

	/**
	 * Refunds a previous transaction. This works against payments that have been settled.
	 *
	 * @param priorPayment the prior payment data
	 * @param amount the amount to refund
	 * @return the payment response
	 * @throws ProtectPayException if an error is returned from ProtectPay
	 */
	public PaymentResponse refund( PriorPayment priorPayment, Integer amount) throws ProtectPayException {
		RefundRequest request = new RefundRequest();
		request.setComment1(
				contractsFactory.createRefundRequestComment1(priorPayment.getComment1()));
		request.setComment2(
				contractsFactory.createRefundRequestComment2(priorPayment.getComment2()));
		if (priorPayment.getMerchantProfileId() != null) {
			request.setMerchantProfileId(
					contractsFactory.createRefundRequestMerchantProfileId(
							priorPayment.getMerchantProfileId().toString()));
		}
		request.setOriginalTransactionId(
				contractsFactory.createRefundRequestOriginalTransactionId(priorPayment.getOriginalTransactionId()));
		request.setTransactionHistoryId(priorPayment.getTransactionHistoryId());
		request.setAmount(amount);
		TransactionResult response = service.refundPaymentV2(id, request);
		Result result = response.getRequestResult().getValue();
		checkResult(result);
		TransactionInformation info = response.getTransaction().getValue();
		return toPaymentResponse(info);
	}

	/**
	 * Executes a credit transaction. Credit transactions require no prior transaction details, but may
	 * not be enabled on your account.
	 *
	 * @param payment the payment data
	 * @param cco any overrides
	 * @return the payment response
	 * @throws ProtectPayException if an error is returned from ProtectPay
	 */
	public PaymentResponse credit(Payment payment, CreditCardOverride cco) throws ProtectPayException {
		return transact(payment, cco, null, false, Type.CREDIT);
	}

	/**
	 * Executes a credit transaction. Credit transactions require no prior transaction details, but may
	 * not be enabled on your account.
	 *
	 * @param payment the payment data
	 * @param ao any ovrrrides
	 * @return the payment response
	 * @throws ProtectPayException if an error is returned from ProtectPay
	 */
	public PaymentResponse credit(Payment payment, ACHOverride ao) throws ProtectPayException {
		return transact(payment, null, ao, false, Type.CREDIT);
	}

	/**
	 * Executes a credit transaction. Credit transactions require no prior transaction details, but may
	 * not be enabled on your account.
	 *
	 * @param payment the payment data
	 * @return the payment response
	 * @throws ProtectPayException if an error is returned from ProtectPay
	 */
	public PaymentResponse credit(Payment payment) throws ProtectPayException {
		return transact(payment, null, null, false, Type.CREDIT);
	}

	/**
	 * Returns a temporary token.
	 *
	 * @param payerAccountId the payment account ID
	 * @param payerName the payer name identifier
	 * @param duration the duration in seconds the token is valid for
	 * @return the token
	 * @throws ProtectPayException if an error is returned from ProtectPay
	 */
	public String getTempToken(String payerAccountId, String payerName, Integer duration)
			throws ProtectPayException {
		TempTokenRequest request = new TempTokenRequest();
		request.setIdentification(typesFactory.createTempTokenRequestIdentification(id));
		PayerInformation payerInfo = new PayerInformation();
		payerInfo.setId(typesFactory.createPayerInformationId(payerAccountId));
		payerInfo.setName(typesFactory.createPayerInformationName(payerName));
		request.setPayerInfo(typesFactory.createTempTokenRequestPayerInfo(payerInfo));
		TempTokenProperties properties = new TempTokenProperties();
		if (duration != null) {
			properties.setDurationSeconds(duration);
		}
		request.setTokenProperties(typesFactory.createTempTokenRequestTokenProperties(properties));
		TempTokenResult response = service.getTempToken(request);
		Result result = response.getRequestResult().getValue();
		checkResult(result);
		return response.getTempToken().getValue();
	}

	/**
	 * Creates a merchant profile.
	 *
	 * @param paymentProcessor the payment processor
	 * @param profileName the profile name
	 * @param processorDatum processor specific data
	 * @return the ID of the merchant profile
	 * @throws ProtectPayException if an error is returned from ProtectPay
	 */
	public Long createMerchantProfile(String paymentProcessor, String profileName,
			Map<String, String> processorDatum) throws ProtectPayException {
		MerchantProfileData data = new MerchantProfileData();
		data.setPaymentProcessor(contractsFactory.createMerchantProfileDataPaymentProcessor(paymentProcessor));
		data.setProfileName(contractsFactory.createMerchantProfileDataProfileName(profileName));
		ArrayOfProcessorDatum aopd = new ArrayOfProcessorDatum();
		List<ProcessorDatum> pds = aopd.getProcessorDatum();
		for (String field : processorDatum.keySet()) {
			String value = processorDatum.get(field);
			ProcessorDatum pd = new ProcessorDatum();
			pd.setProcessorField(contractsFactory.createProcessorDatumProcessorField(field));
			pd.setValue(contractsFactory.createProcessorDatumValue(value));
			pds.add(pd);
		}
		data.setProcessorData(contractsFactory.createMerchantProfileDataProcessorData(aopd));
		CreateMerchantProfileResult response = service.createMerchantProfile(id, data);
		Result result = response.getRequestResult().getValue();
		checkResult(result);
		return response.getProfileId();
	}
}