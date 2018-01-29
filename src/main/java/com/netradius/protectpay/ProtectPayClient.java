package com.netradius.protectpay;

import com.netradius.protectpay.ProtectPayBillingInfo.Country;
import com.propay.sps.SPS;
import com.propay.sps.SPSService;
import com.propay.sps.types.ArrayOfPayerInfo;
import com.propay.sps.types.ArrayOfPaymentMethodInformation;
import com.propay.sps.types.Billing;
import com.propay.sps.types.CreateAccountInformationResult;
import com.propay.sps.types.CreatePaymentMethodResult;
import com.propay.sps.types.GetPayersResult;
import com.propay.sps.types.ID;
import com.propay.sps.types.ObjectFactory;
import com.propay.sps.types.PayerData;
import com.propay.sps.types.PayerInfo;
import com.propay.sps.types.PayerInformation;
import com.propay.sps.types.PaymentMethodAdd;
import com.propay.sps.types.PaymentMethodInformation;
import com.propay.sps.types.PaymentMethodUpdate;
import com.propay.sps.types.PaymentMethodsResult;
import com.propay.sps.types.Result;
import com.propay.sps.types.TempTokenProperties;
import com.propay.sps.types.TempTokenRequest;
import com.propay.sps.types.TempTokenResult;
import com.propay.sps.types.Transaction;
import com.propay.sps.types.TransactionInformation;
import com.propay.sps.types.TransactionResult;
import org.datacontract.schemas._2004._07.propay_contracts_sps.AchOverrides;
import org.datacontract.schemas._2004._07.propay_contracts_sps.ArrayOfProcessorDatum;
import org.datacontract.schemas._2004._07.propay_contracts_sps.CaptureRequest;
import org.datacontract.schemas._2004._07.propay_contracts_sps.CreateMerchantProfileResult;
import org.datacontract.schemas._2004._07.propay_contracts_sps.CreditCardOverrides;
import org.datacontract.schemas._2004._07.propay_contracts_sps.EditPayerRequest;
import org.datacontract.schemas._2004._07.propay_contracts_sps.MerchantProfileData;
import org.datacontract.schemas._2004._07.propay_contracts_sps.PayerOverrides;
import org.datacontract.schemas._2004._07.propay_contracts_sps.PaymentInfoOverrides;
import org.datacontract.schemas._2004._07.propay_contracts_sps.ProcessorDatum;
import org.datacontract.schemas._2004._07.propay_contracts_sps.RefundRequest;
import org.datacontract.schemas._2004._07.propay_contracts_sps.VoidRequest;
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

  public static final String PRODUCTION_URL = "https://protectpay.propay.com/API/SPS.svc?wsdl";
  public static final String TESTING_URL = "https://xmltestapi.propay.com/protectpay/sps.svc?wsdl";

  private static final Logger log = LoggerFactory.getLogger(ProtectPayClient.class);

  private SPSService service;
  private ObjectFactory typesFactory;
  private org.datacontract.schemas._2004._07.propay_contracts_sps.ObjectFactory contractsFactory;
  private ID id;
  private URL serviceUrl;

  private enum Type {
    AUTH,
    CAPTURE,
    CREDIT
  }

  /**
   * Creates a new ProtectPayClient instance.
   *
   * @param wsdlUrl             the WSDL URL to use
   * @param authenticationToken the authentication token assigned by ProPay
   * @param billerAccountId     the biller account ID assigned by ProPay
   */
  public ProtectPayClient(String wsdlUrl, String authenticationToken, String billerAccountId) {
    try {
      serviceUrl = new URL(wsdlUrl);
    } catch (MalformedURLException x) {
      throw new IllegalArgumentException("Invalid URL [" + wsdlUrl + "]: " + x.getMessage(), x);
    }
    typesFactory = new ObjectFactory();
    contractsFactory = new org.datacontract.schemas._2004._07.propay_contracts_sps.ObjectFactory();
    id = typesFactory.createID();
    id.setAuthenticationToken(typesFactory.createIDAuthenticationToken(authenticationToken));
    id.setBillerAccountId(typesFactory.createIDBillerAccountId(billerAccountId));
  }

  private void initService() {
    try {
      SPS sps = new SPS(serviceUrl);
      service = sps.getBasicHttpBindingSPSService();
    } catch (Exception x) {
      throw new IllegalArgumentException("Error initializng service: [" + serviceUrl
          + "]: " + x.getMessage(), x);
    }
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

  private PayerData toPayerData(ProtectPayPayer protectPayPayer) {
    PayerData data = typesFactory.createPayerData();
    data.setName(typesFactory.createPayerDataName(protectPayPayer.getAccountName()));
    data.setEmailAddress(typesFactory.createPayerDataEmailAddress(protectPayPayer
        .getEmailAddress()));
    data.setExternalId1(typesFactory.createPayerDataExternalId1(protectPayPayer.getExternalId1()));
    data.setExternalId2(typesFactory.createPayerDataExternalId2(protectPayPayer.getExternalId2()));
    return data;
  }

  private Billing toBilling(ProtectPayBillingInfo info) {
    Billing billing = new Billing();
    billing.setAddress1(typesFactory.createBillingAddress1(info.getAddress1()));
    billing.setAddress2(typesFactory.createBillingAddress2(info.getAddress2()));
    billing.setAddress3(typesFactory.createBillingAddress3(info.getAddress3()));
    billing.setCity(typesFactory.createBillingCity(info.getCity()));
    billing.setState(typesFactory.createBillingState(info.getState()));
    billing.setZipCode(typesFactory.createBillingZipCode(info.getZipCode()));
    billing.setCountry(typesFactory.createBillingCountry(info.getCountry().toString()));
    billing.setEmail(typesFactory.createBillingEmail(info.getEmailAddress()));
    billing.setTelephoneNumber(typesFactory.createBillingTelephoneNumber(info
        .getTelephoneNumber()));
    return billing;
  }

  private ProtectPayBillingInfo toBillingInfo(Billing billing) {
    ProtectPayBillingInfo info = new ProtectPayBillingInfo();
    info.setAddress1(billing.getAddress1().getValue());
    info.setAddress2(billing.getAddress2().getValue());
    info.setAddress3(billing.getAddress3().getValue());
    info.setEmailAddress(billing.getEmail().getValue());
    info.setCity(billing.getCity().getValue());
    info.setState(billing.getState().getValue());
    info.setCountry(Country.valueOf(billing.getCountry().getValue()));
    info.setTelephoneNumber(billing.getTelephoneNumber().getValue());
    info.setZipCode(billing.getZipCode().getValue());
    return info;
  }

  private ProtectPayPaymentMethod toPaymentMethod(PaymentMethodInformation info) {
    ProtectPayPaymentMethod protectPayPaymentMethod = new ProtectPayPaymentMethod();
    protectPayPaymentMethod.setAccountName(info.getAccountName().getValue());
    protectPayPaymentMethod.setDateCreated(info.getDateCreated().toGregorianCalendar().getTime());
    protectPayPaymentMethod.setDescription(info.getDescription().getValue());
    protectPayPaymentMethod.setExpirationDate(info.getExpirationDate().getValue());
    protectPayPaymentMethod.setAccountNumber(info.getObfuscatedAccountNumber().getValue());
    protectPayPaymentMethod.setPaymentMethodId(info.getPaymentMethodID().getValue());
    protectPayPaymentMethod.setType(ProtectPayPaymentMethod.Type.valueOf(info
        .getPaymentMethodType().getValue()));
    protectPayPaymentMethod.setPriority(info.getPriority());
    Billing billing = info.getBillingInformation().getValue();
    if (billing != null) {
      protectPayPaymentMethod.setBilling(toBillingInfo(billing));
    }
    return protectPayPaymentMethod;
  }

  private Transaction toTransaction(ProtectPayPayment protectPayPayment) {
    Transaction transaction = new Transaction();
    transaction.setPayerAccountId(typesFactory.createTransactionPayerAccountId(protectPayPayment
        .getPayerAccountId()));
    transaction.setAmount(typesFactory.createTransactionAmount(protectPayPayment.getAmount()
        .toString()));
    transaction.setComment1(typesFactory.createTransactionComment1(protectPayPayment
        .getComment1()));
    transaction.setComment2(typesFactory.createTransactionComment2(protectPayPayment
        .getComment2()));
    transaction.setCurrencyCode(typesFactory.createTransactionCurrencyCode(protectPayPayment
        .getCurrencyCode()));
    // This is not supported any more with latest wsdl
    // transaction.setInputIpAddress(typesFactory.createTransactionInputIpAddress(
    // protectPayPayment.getInputIpAddress()));
    transaction.setInvoice(typesFactory.createTransactionInvoice(protectPayPayment.getInvoice()));
    if (protectPayPayment.getMerchantProfileId() != null) {
      transaction.setMerchantProfileId(
          typesFactory.createTransactionMerchantProfileId(protectPayPayment.getMerchantProfileId()
              .toString()));
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
      overrides.setBilling(contractsFactory.createCreditCardOverridesBilling(toBilling(cco
          .getBilling())));
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

  private PayerOverrides toPayerOverrides(String ipAddress) {
    PayerOverrides overrides = new PayerOverrides();
    if (ipAddress != null && !ipAddress.isEmpty()) {
      overrides.setIpAddress(contractsFactory.createPayerOverridesIpAddress(ipAddress));
    }
    return overrides;
  }

  private ProtectPayPaymentResponse toPaymentResponse(TransactionInformation info) {
    ProtectPayPaymentResponse protectPayPaymentResponse = new ProtectPayPaymentResponse();
    protectPayPaymentResponse.setAuthorizationCode(info.getAuthorizationCode().getValue());
    protectPayPaymentResponse.setAvsCode(info.getAVSCode().getValue());
    protectPayPaymentResponse.setConversionRate(info.getCurrencyConversionRate());
    protectPayPaymentResponse.setConvertedAmount(info.getCurrencyConvertedAmount());
    protectPayPaymentResponse.setConvertedCurrencyCode(info.getCurrencyConvertedCurrencyCode()
        .getValue());
    Result result = info.getResultCode().getValue();
    if (result != null) {
      protectPayPaymentResponse.setResultCode(result.getResultCode().getValue());
      protectPayPaymentResponse.setResultMessage(result.getResultMessage().getValue());
      protectPayPaymentResponse.setResultValue(result.getResultValue().getValue());
    }
    if (info.getTransactionHistoryId() != null) {
      protectPayPaymentResponse.setTransactionHistoryId(Long.parseLong(info
          .getTransactionHistoryId().getValue()));
    }
    protectPayPaymentResponse.setTransactionId(info.getTransactionId().getValue());
    protectPayPaymentResponse.setTransactionResult(info.getTransactionResult().getValue());
    return protectPayPaymentResponse;
  }

  /**
   * Creates a new payer.
   *
   * @param accountName the account name used to identify this payer
   * @return the payer account ID generated by ProtectPay
   * @throws ProtectPayException if the request fails
   */
  public String createPayer(String accountName) throws ProtectPayException {
    if (service == null) {
      initService();
    }
    CreateAccountInformationResult response = service.createPayer(id, accountName);
    Result result = response.getRequestResult().getValue();
    checkResult(result);
    return response.getExternalAccountID().getValue();
  }

  /**
   * Creates a new payer. This method will return the generated payer account ID as well
   * as set the value on the Payer argument.
   *
   * @param protectPayPayer the payer to create
   * @return the payer account ID generated by ProtectPay
   * @throws ProtectPayException if the request fails
   */
  public String createPayer(ProtectPayPayer protectPayPayer) throws ProtectPayException {
    if (service == null) {
      initService();
    }
    CreateAccountInformationResult response = service.createPayerWithData(id,
        toPayerData(protectPayPayer));
    Result result = response.getRequestResult().getValue();
    checkResult(result);
    String accountId = response.getExternalAccountID().getValue();
    protectPayPayer.setPayerAccountId(accountId);
    return accountId;
  }

  /**
   * Updates an existing payer.
   *
   * @param protectPayPayer the payer to update with the data
   * @throws ProtectPayException if the request fails
   */
  public void updatePayer(ProtectPayPayer protectPayPayer) throws ProtectPayException {
    if (service == null) {
      initService();
    }
    EditPayerRequest request = new EditPayerRequest();
    request.setPayerAccountId(contractsFactory.createEditPayerRequestPayerAccountId(protectPayPayer
        .getPayerAccountId()));
    request.setUpdatedData(contractsFactory.createEditPayerRequestUpdatedData(toPayerData(
        protectPayPayer)));
    Result result = service.editPayerV2(id, request);
    checkResult(result);
  }

  /**
   * Deletes a payer.
   *
   * @param payerAccountId the payer account ID to delete
   * @throws ProtectPayException if the request fails
   */
  public void deletePayer(String payerAccountId) throws ProtectPayException {
    if (service == null) {
      initService();
    }
    Result result = service.deletePayer(id, payerAccountId);
    checkResult(result);
  }

  /**
   * Searches for payers.
   *
   * @param protectPayPayer the data to use as a criteria
   * @return the matching payers
   * @throws ProtectPayException if the request fails
   */
  public List<ProtectPayPayer> getPayers(ProtectPayPayer protectPayPayer) throws
      ProtectPayException {
    if (service == null) {
      initService();
    }
    GetPayersResult response = service.getPayers(id, protectPayPayer == null ? null
        : toPayerData(protectPayPayer));
    Result result = response.getRequestResult().getValue();
    checkResult(result);
    ArrayOfPayerInfo aopi = response.getPayers().getValue();
    if (aopi != null) {
      List<ProtectPayPayer> protectPayPayers = new ArrayList<>(aopi.getPayerInfo().size());
      for (PayerInfo info : aopi.getPayerInfo()) {
        ProtectPayPayer foundProtectPayPayer = new ProtectPayPayer();
        foundProtectPayPayer.setPayerAccountId(info.getPayerAccountId().getValue());
        foundProtectPayPayer.setAccountName(info.getName().getValue());
        foundProtectPayPayer.setExternalId1(info.getExternalId1().getValue());
        foundProtectPayPayer.setExternalId2(info.getExternalId2().getValue());
        protectPayPayers.add(foundProtectPayPayer);
      }
      return protectPayPayers;
    }
    return new ArrayList<>(0);
  }

  /**
   * Creates a new payment method. This method will return the generated payment method ID
   * as well as set the value on the PaymentMethod argument.
   *
   * @param protectPayPaymentMethod the payment method to create
   * @return the generated payment method ID
   * @throws ProtectPayException if the request fails
   */
  public String createPaymentMethod(ProtectPayPaymentMethod protectPayPaymentMethod) throws
      ProtectPayException {
    if (service == null) {
      initService();
    }
    PaymentMethodAdd request = new PaymentMethodAdd();
    if (protectPayPaymentMethod.getAccountCountryCode() != null) {
      request.setAccountCountryCode(typesFactory.createPaymentMethodAddAccountCountryCode(
          protectPayPaymentMethod.getAccountCountryCode().getValue()));
    }
    request.setAccountName(typesFactory.createPaymentMethodAddAccountName(protectPayPaymentMethod
        .getAccountName()));
    request.setAccountNumber(typesFactory.createPaymentMethodAddAccountNumber(
        protectPayPaymentMethod.getAccountNumber()));
    request.setBankNumber(typesFactory.createPaymentMethodAddBankNumber(protectPayPaymentMethod
        .getBankNumber()));
    request.setDescription(typesFactory.createPaymentMethodAddDescription(protectPayPaymentMethod
        .getDescription()));
    if (protectPayPaymentMethod.getDuplicateAction() != null) {
      request.setDuplicateAction(typesFactory.createPaymentMethodAddDuplicateAction(
          protectPayPaymentMethod.getDuplicateAction().toString()));
    }
    request.setExpirationDate(typesFactory.createPaymentMethodAddExpirationDate(
        protectPayPaymentMethod.getExpirationDate()));
    request.setPayerAccountId(typesFactory.createPaymentMethodAddPayerAccountId(
        protectPayPaymentMethod.getPayerAccountId()));
    if (protectPayPaymentMethod.getType() != null) {
      request.setPaymentMethodType(typesFactory.createPaymentMethodAddPaymentMethodType(
          protectPayPaymentMethod.getType().toString()));
    }
    if (protectPayPaymentMethod.getPayerProtected() != null) {
      request.setProtected(protectPayPaymentMethod.getPayerProtected());
    }
    request.setPriority(protectPayPaymentMethod.getPriority());
    if (protectPayPaymentMethod.getBilling() != null) {
      request.setBillingInformation(typesFactory.createBilling(toBilling(protectPayPaymentMethod
          .getBilling())));
    }

    CreatePaymentMethodResult response = service.createPaymentMethod(id, request);
    Result result = response.getRequestResult().getValue();
    checkResult(result);
    String paymentMethodId = response.getPaymentMethodId().getValue();
    protectPayPaymentMethod.setPaymentMethodId(paymentMethodId);
    return paymentMethodId;
  }

  /**
   * Updated a payment method. While this method accepts a payment method as an argument,
   * not all the fields are available to be updated. The following fields are not available
   * for updates: accountCountryCode, accountNumber, bankNumber and type (if credit card).
   *
   * @param protectPayPaymentMethod the payment method to update
   * @throws ProtectPayException if the request fails
   */
  public void updatePaymentMethod(ProtectPayPaymentMethod protectPayPaymentMethod) throws
      ProtectPayException {
    if (service == null) {
      initService();
    }
    PaymentMethodUpdate request = new PaymentMethodUpdate();
    request.setPayerAccountId(typesFactory.createPaymentMethodUpdatePayerAccountId(
        protectPayPaymentMethod.getPayerAccountId()));
    request.setAccountName(typesFactory.createPaymentMethodUpdateAccountName(
        protectPayPaymentMethod.getAccountName()));
    if (protectPayPaymentMethod.getType() == ProtectPayPaymentMethod.Type.Checking
        || protectPayPaymentMethod.getType() == ProtectPayPaymentMethod.Type.Savings) {
      request.setBankAccountType(typesFactory.createPaymentMethodUpdateBankAccountType(
          protectPayPaymentMethod.getType().toString()));
    }
    request.setDescription(typesFactory.createPaymentMethodUpdateDescription(
        protectPayPaymentMethod.getDescription()));
    request.setExpirationDate(typesFactory.createPaymentMethodUpdateExpirationDate(
        protectPayPaymentMethod.getExpirationDate()));
    request.setPaymentMethodID(typesFactory.createPaymentMethodUpdatePaymentMethodID(
        protectPayPaymentMethod.getPaymentMethodId()));
    if (protectPayPaymentMethod.getPayerProtected() != null) {
      request.setProtected(typesFactory.createPaymentMethodUpdateProtected(protectPayPaymentMethod
          .getPayerProtected()));
    }
    if (protectPayPaymentMethod.getBilling() != null) {
      request.setBillingInformation(typesFactory.createPaymentMethodUpdateBillingInformation(
          toBilling(protectPayPaymentMethod.getBilling())));
    }
    Result result = service.editPaymentMethod(id, request);
    checkResult(result);
  }

  /**
   * Deletes a payment method.
   *
   * @param payerAccountId  the payer account ID the payment method belongs to
   * @param paymentMethodId the payment method ID to delete
   * @throws ProtectPayException if the request fails
   */
  public void deletePaymentMethod(String payerAccountId, String paymentMethodId) throws
      ProtectPayException {
    if (service == null) {
      initService();
    }
    Result result = service.deletePaymentMethod(id, payerAccountId, paymentMethodId);
    checkResult(result);
  }

  /**
   * Finds all the payment methods associated with a payer account.
   *
   * @param payerAccountId the payer account ID
   * @return all payment methods associated with the payer account
   * @throws ProtectPayException if the request fails
   */
  public List<ProtectPayPaymentMethod> getPaymentMethods(String payerAccountId) throws
      ProtectPayException {
    if (service == null) {
      initService();
    }
    PaymentMethodsResult response = service.getAllPayerPaymentMethods(id, payerAccountId);
    Result result = response.getRequestResult().getValue();
    checkResult(result);
    ArrayOfPaymentMethodInformation aopmi = response.getPaymentMethods().getValue();
    if (aopmi != null) {
      List<ProtectPayPaymentMethod> protectPayPaymentMethods = new ArrayList<>(aopmi
          .getPaymentMethodInformation().size());
      for (PaymentMethodInformation info : aopmi.getPaymentMethodInformation()) {
        protectPayPaymentMethods.add(toPaymentMethod(info));
      }
      return protectPayPaymentMethods;
    } else {
      return new ArrayList<>(0);
    }
  }

  /**
   * Finds a payment method.
   *
   * @param payerAccountId  the payer account ID
   * @param paymentMethodId the payment method ID
   * @return the found payment method or null
   * @throws ProtectPayException if the request fails
   */
  public ProtectPayPaymentMethod getPaymentMethod(String payerAccountId, String paymentMethodId)
      throws ProtectPayException {
    if (service == null) {
      initService();
    }
    PaymentMethodsResult response = service.getPayerPaymentMethod(id, payerAccountId,
        paymentMethodId);
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

  private ProtectPayPaymentResponse transact(ProtectPayPayment protectPayPayment,
                                             CreditCardOverride cco, ACHOverride ao,
                                             boolean recurring, Type type)
      throws ProtectPayException {
    if (service == null) {
      initService();
    }
    Transaction transaction = toTransaction(protectPayPayment);
    PaymentInfoOverrides paymentInfoOverrides = new PaymentInfoOverrides();
    if (cco != null) {
      paymentInfoOverrides.setCreditCard(
          contractsFactory.createPaymentInfoOverridesCreditCard(toCreditCardOverrides(cco)));
    }
    if (ao != null) {
      paymentInfoOverrides.setAch(contractsFactory.createPaymentInfoOverridesAch(
          toAchOverrides(ao)));
    }
    if (protectPayPayment.getInputIpAddress() != null
        && !protectPayPayment.getInputIpAddress().isEmpty()) {
      paymentInfoOverrides.setPayer(contractsFactory.createPayerOverrides(
          toPayerOverrides(protectPayPayment.getInputIpAddress())));
    }
    TransactionResult response = null;
    switch (type) {
      case AUTH:
        if (recurring) {
          response = service.authorizePaymentMethodTransactionRecurring(
              id, transaction, protectPayPayment.getPaymentMethodId(), paymentInfoOverrides);
        } else {
          response = service.authorizePaymentMethodTransaction(
              id, transaction, protectPayPayment.getPaymentMethodId(), paymentInfoOverrides);
        }
        break;
      case CAPTURE:
        if (recurring) {
          response = service.processPaymentMethodTransactionRecurring(
              id, transaction, protectPayPayment.getPaymentMethodId(), paymentInfoOverrides);
        } else {
          response = service.processPaymentMethodTransaction(
              id, transaction, protectPayPayment.getPaymentMethodId(), paymentInfoOverrides);
        }
        break;
      case CREDIT:
        response = service.creditPayment(
            id, transaction, protectPayPayment.getPaymentMethodId(), paymentInfoOverrides);
        break;
      default:
        // added the default to fix the MissingSwitchDefault checkstyle violation
        throw new ProtectPayException("", "FAILURE", "Unrecognised cast " + type);
    }
    Result result = response.getRequestResult().getValue();
    checkResult(result);
    TransactionInformation info = response.getTransaction().getValue();
    return toPaymentResponse(info);
  }

  /**
   * Executes an auth transaction.
   *
   * @param protectPayPayment the payment data
   * @param override          any override values
   * @param recurring         true if recurring, false if otherwise
   * @return the payment response
   * @throws ProtectPayException if an error is returned from ProtectPay
   */
  public ProtectPayPaymentResponse auth(ProtectPayPayment protectPayPayment, CreditCardOverride
      override, boolean recurring) throws ProtectPayException {
    return transact(protectPayPayment, override, null, recurring, Type.AUTH);
  }

  /**
   * Executes an auth transaction.
   *
   * @param protectPayPayment the payment data
   * @param cvv               the CVV value
   * @param recurring         true if recurring, false if otherwise
   * @return the payment response
   * @throws ProtectPayException if an error is returned from ProtectPay
   */
  public ProtectPayPaymentResponse auth(ProtectPayPayment protectPayPayment, String cvv,
                                        boolean recurring) throws ProtectPayException {
    CreditCardOverride cco = new CreditCardOverride();
    cco.setCvv(cvv);
    return transact(protectPayPayment, cco, null, recurring, Type.AUTH);
  }

  /**
   * Executes an auth transaction.
   *
   * @param protectPayPayment the payment data
   * @param override          any override values
   * @param recurring         true if recurring, false if otherwise
   * @return the payment response
   * @throws ProtectPayException if an error is returned from ProtectPay
   */
  public ProtectPayPaymentResponse auth(ProtectPayPayment protectPayPayment, ACHOverride override,
                                        boolean recurring) throws ProtectPayException {
    return transact(protectPayPayment, null, override, recurring, Type.AUTH);
  }

  /**
   * Executes an auth transaction.
   *
   * @param protectPayPayment the payment data
   * @param recurring         true if recurring, false if otherwise
   * @return the payment response
   * @throws ProtectPayException if an error is returned from ProtectPay
   */
  public ProtectPayPaymentResponse auth(ProtectPayPayment protectPayPayment, boolean recurring)
      throws ProtectPayException {
    return transact(protectPayPayment, null, null, recurring, Type.AUTH);
  }

  /**
   * Executes a capture transaction.
   *
   * @param payment the prior payment data
   * @param amount  the amount to capture
   * @return the payment response
   * @throws ProtectPayException if an error is returned from ProtectPay
   */
  public ProtectPayPaymentResponse capture(ProtectPayPriorPayment payment, int amount)
      throws ProtectPayException {
    if (service == null) {
      initService();
    }
    CaptureRequest request = new CaptureRequest();
    if (payment.getMerchantProfileId() != null) {
      request.setMerchantProfileId(
          contractsFactory.createCaptureRequestMerchantProfileId(payment.getMerchantProfileId()
              .toString()));
    }
    request.setComment1(contractsFactory.createCaptureRequestComment1(payment.getComment1()));
    request.setComment2(contractsFactory.createCaptureRequestComment2(payment.getComment2()));
    request.setAmount(amount);
    request.setOriginalTransactionId(
        contractsFactory.createCaptureRequestOriginalTransactionId(payment
            .getOriginalTransactionId()));
    request.setTransactionHistoryId(payment.getTransactionHistoryId());
    TransactionResult response = service.capturePaymentV2(id, request);
    Result result = response.getRequestResult().getValue();
    checkResult(result);
    TransactionInformation info = response.getTransaction().getValue();
    return toPaymentResponse(info);
  }

  private ProtectPayPaymentResponse authAndCapture(ProtectPayPayment protectPayPayment,
                                                   CreditCardOverride cco, ACHOverride ao,
                                                   boolean recurring)
      throws ProtectPayException {
    return transact(protectPayPayment, cco, ao, recurring, Type.CAPTURE);
  }

  /**
   * Executes an auth and capture request.
   *
   * @param protectPayPayment the payment data
   * @param cco               any override values
   * @param recurring         true if recurring, false if otherwise
   * @return the payment response
   * @throws ProtectPayException if an error is returned from ProtectPay
   */
  public ProtectPayPaymentResponse authAndCapture(ProtectPayPayment protectPayPayment,
                                                  CreditCardOverride cco, boolean recurring)
      throws ProtectPayException {
    return transact(protectPayPayment, cco, null, recurring, Type.CAPTURE);
  }

  /**
   * Executes and auth and capture transaction.
   *
   * @param protectPayPayment the payment data
   * @param cvv               the CVV value
   * @param recurring         true if recurring, false if otherwise
   * @return the payment response
   * @throws ProtectPayException if an error is returned from ProtectPay
   */
  public ProtectPayPaymentResponse authAndCapture(ProtectPayPayment protectPayPayment, String cvv,
                                                  boolean recurring)
      throws ProtectPayException {
    CreditCardOverride cco = new CreditCardOverride();
    cco.setCvv(cvv);
    return transact(protectPayPayment, cco, null, recurring, Type.CAPTURE);
  }

  /**
   * Executes an auth and capture transaction.
   *
   * @param protectPayPayment the payment data
   * @param ao                any override values
   * @param recurring         true if recurring, false if otherwise
   * @return the payment response
   * @throws ProtectPayException if an error is returned from ProtectPay
   */
  public ProtectPayPaymentResponse authAndCapture(ProtectPayPayment protectPayPayment,
                                                  ACHOverride ao, boolean recurring)
      throws ProtectPayException {
    return transact(protectPayPayment, null, ao, recurring, Type.CAPTURE);
  }

  /**
   * Executes an auth and capture transaction.
   *
   * @param protectPayPayment the payment data
   * @param recurring         true if recurring, false if otherwise
   * @return the payment response
   * @throws ProtectPayException if an error is returned from ProtectPay
   */
  public ProtectPayPaymentResponse authAndCapture(ProtectPayPayment protectPayPayment,
                                                  boolean recurring)
      throws ProtectPayException {
    return transact(protectPayPayment, null, null, recurring, Type.CAPTURE);
  }

  /**
   * Voids a previous transaction. This generally only works against transaction that have gone
   * through auth, but not capture.
   *
   * @param protectPayPriorPayment the prior payment data
   * @return the payment response
   * @throws ProtectPayException if an error is returned from ProtectPay
   */
  public ProtectPayPaymentResponse voidPayment(ProtectPayPriorPayment protectPayPriorPayment)
      throws ProtectPayException {
    if (service == null) {
      initService();
    }
    VoidRequest request = new VoidRequest();
    request.setOriginalTransactionId(
        contractsFactory.createVoidRequestOriginalTransactionId(protectPayPriorPayment
            .getOriginalTransactionId()));
    request.setTransactionHistoryId(protectPayPriorPayment.getTransactionHistoryId());
    request.setComment1(
        contractsFactory.createVoidRequestComment1(protectPayPriorPayment.getComment1()));
    request.setComment2(
        contractsFactory.createVoidRequestComment2(protectPayPriorPayment.getComment2()));
    if (protectPayPriorPayment.getMerchantProfileId() != null) {
      request.setMerchantProfileId(
          contractsFactory.createVoidRequestMerchantProfileId(
              protectPayPriorPayment.getMerchantProfileId().toString()));
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
   * @param protectPayPriorPayment the prior payment data
   * @param amount                 the amount to refund
   * @return the payment response
   * @throws ProtectPayException if an error is returned from ProtectPay
   */
  public ProtectPayPaymentResponse refund(ProtectPayPriorPayment protectPayPriorPayment,
                                          Integer amount) throws ProtectPayException {
    if (service == null) {
      initService();
    }
    RefundRequest request = new RefundRequest();
    request.setComment1(
        contractsFactory.createRefundRequestComment1(protectPayPriorPayment.getComment1()));
    request.setComment2(
        contractsFactory.createRefundRequestComment2(protectPayPriorPayment.getComment2()));
    if (protectPayPriorPayment.getMerchantProfileId() != null) {
      request.setMerchantProfileId(
          contractsFactory.createRefundRequestMerchantProfileId(
              protectPayPriorPayment.getMerchantProfileId().toString()));
    }
    request.setOriginalTransactionId(
        contractsFactory.createRefundRequestOriginalTransactionId(protectPayPriorPayment
            .getOriginalTransactionId()));
    request.setTransactionHistoryId(protectPayPriorPayment.getTransactionHistoryId());
    request.setAmount(amount);
    TransactionResult response = service.refundPaymentV2(id, request);
    Result result = response.getRequestResult().getValue();
    checkResult(result);
    TransactionInformation info = response.getTransaction().getValue();
    return toPaymentResponse(info);
  }

  /**
   * Executes a credit transaction. Credit transactions require no prior transaction details,
   * but may not be enabled on your account.
   *
   * @param protectPayPayment the payment data
   * @param cco               any overrides
   * @return the payment response
   * @throws ProtectPayException if an error is returned from ProtectPay
   */
  public ProtectPayPaymentResponse credit(ProtectPayPayment protectPayPayment, CreditCardOverride
      cco) throws ProtectPayException {
    return transact(protectPayPayment, cco, null, false, Type.CREDIT);
  }

  /**
   * Executes a credit transaction. Credit transactions require no prior transaction details,
   * but may not be enabled on your account.
   *
   * @param protectPayPayment the payment data
   * @param ao                any ovrrrides
   * @return the payment response
   * @throws ProtectPayException if an error is returned from ProtectPay
   */
  public ProtectPayPaymentResponse credit(ProtectPayPayment protectPayPayment, ACHOverride ao)
      throws ProtectPayException {
    return transact(protectPayPayment, null, ao, false, Type.CREDIT);
  }

  /**
   * Executes a credit transaction. Credit transactions require no prior transaction details,
   * but may not be enabled on your account.
   *
   * @param protectPayPayment the payment data
   * @return the payment response
   * @throws ProtectPayException if an error is returned from ProtectPay
   */
  public ProtectPayPaymentResponse credit(ProtectPayPayment protectPayPayment) throws
      ProtectPayException {
    return transact(protectPayPayment, null, null, false, Type.CREDIT);
  }

  /**
   * Returns a temporary token.
   *
   * @param payerAccountId the payment account ID
   * @param payerName      the payer name identifier
   * @param duration       the duration in seconds the token is valid for
   * @return the token
   * @throws ProtectPayException if an error is returned from ProtectPay
   */
  public String getTempToken(String payerAccountId, String payerName, Integer duration)
      throws ProtectPayException {
    if (service == null) {
      initService();
    }
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
   * @param profileName      the profile name
   * @param processorDatum   processor specific data
   * @return the ID of the merchant profile
   * @throws ProtectPayException if an error is returned from ProtectPay
   */
  public Long createMerchantProfile(String paymentProcessor, String profileName,
                                    Map<String, String> processorDatum) throws
      ProtectPayException {
    if (service == null) {
      initService();
    }
    MerchantProfileData data = new MerchantProfileData();
    data.setPaymentProcessor(contractsFactory.createMerchantProfileDataPaymentProcessor(
        paymentProcessor));
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
