package com.netradius.protectpay;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Holds the ProtectPay Payment response.
 *
 * @author Erik R. Jensen
 */
public class ProtectPayPaymentResponse implements Serializable {

  private static final long serialVersionUID = 81905376598500784L;

  private String authorizationCode;
  private String avsCode;
  private BigDecimal conversionRate;
  private Integer convertedAmount;
  private String convertedCurrencyCode;
  private Long transactionHistoryId;
  private String transactionId;
  private String transactionResult;
  private String resultCode;
  private String resultMessage;
  private String resultValue;

  /**
   * Returns the authorization code.
   *
   * @return the authorization code
   */
  public String getAuthorizationCode() {
    return authorizationCode;
  }

  /**
   * Sets the authorization code.
   *
   * @param authorizationCode the authorization code
   */
  public void setAuthorizationCode(String authorizationCode) {
    this.authorizationCode = authorizationCode;
  }

  /**
   * Returns the AVS code.
   *
   * @return the AVS code
   */
  public String getAvsCode() {
    return avsCode;
  }

  /**
   * Sets the AVS code.
   *
   * @param avsCode the AVS code
   */
  public void setAvsCode(String avsCode) {
    this.avsCode = avsCode;
  }

  /**
   * Returns the conversion rate.
   *
   * @return the conversion rate
   */
  public BigDecimal getConversionRate() {
    return conversionRate;
  }

  /**
   * Sets the conversion rate.
   *
   * @param conversionRate the conversion rate
   */
  public void setConversionRate(BigDecimal conversionRate) {
    this.conversionRate = conversionRate;
  }

  /**
   * Returns the converted amount.
   *
   * @return the converted amount
   */
  public Integer getConvertedAmount() {
    return convertedAmount;
  }

  /**
   * Sets the converted amount.
   *
   * @param convertedAmount the converted amount
   */
  public void setConvertedAmount(Integer convertedAmount) {
    this.convertedAmount = convertedAmount;
  }

  /**
   * Returns the converted currency code.
   *
   * @return the converted currency code
   */
  public String getConvertedCurrencyCode() {
    return convertedCurrencyCode;
  }

  /**
   * Sets the converted currency code.
   *
   * @param convertedCurrencyCode the converted currency code
   */
  public void setConvertedCurrencyCode(String convertedCurrencyCode) {
    this.convertedCurrencyCode = convertedCurrencyCode;
  }

  /**
   * Returns the transaction history ID. This value is assigned by ProtectPay.
   *
   * @return the transaction history ID
   */
  public Long getTransactionHistoryId() {
    return transactionHistoryId;
  }

  /**
   * Sets the transaction history ID. This value is assigned by ProtectPay.
   *
   * @param transactionHistoryId the transaction history ID
   */
  public void setTransactionHistoryId(Long transactionHistoryId) {
    this.transactionHistoryId = transactionHistoryId;
  }

  /**
   * Returns the transaction ID. This value is assigned by the payment gateway or processor.
   *
   * @return the transaction ID
   */
  public String getTransactionId() {
    return transactionId;
  }

  /**
   * Sets the transaction ID. This value is assigned by the payment gateway or processor.
   *
   * @param transactionId the transaction ID
   */
  public void setTransactionId(String transactionId) {
    this.transactionId = transactionId;
  }

  /**
   * Returns the transaction result.
   *
   * @return the transaction result
   */
  public String getTransactionResult() {
    return transactionResult;
  }

  /**
   * Sets the transaction result.
   *
   * @param transactionResult the transaction result
   */
  public void setTransactionResult(String transactionResult) {
    this.transactionResult = transactionResult;
  }

  /**
   * Returns the result code.
   *
   * @return the result code
   */
  public String getResultCode() {
    return resultCode;
  }

  /**
   * Sets the result code.
   *
   * @param resultCode the result code
   */
  public void setResultCode(String resultCode) {
    this.resultCode = resultCode;
  }

  /**
   * Returns the result message.
   *
   * @return the result message
   */
  public String getResultMessage() {
    return resultMessage;
  }

  /**
   * Sets the result message.
   *
   * @param resultMessage the result message
   */
  public void setResultMessage(String resultMessage) {
    this.resultMessage = resultMessage;
  }

  /**
   * Sets the result value.
   *
   * @return the result value
   */
  public String getResultValue() {
    return resultValue;
  }

  /**
   * Sets the result value.
   *
   * @param resultValue the result value
   */
  public void setResultValue(String resultValue) {
    this.resultValue = resultValue;
  }

  @Override
  public String toString() {
    return "PaymentResponse{"
        + "authorizationCode='" + authorizationCode + '\''
        + ", avsCode='" + avsCode + '\''
        + ", conversionRate=" + conversionRate
        + ", convertedAmount=" + convertedAmount
        + ", convertedCurrencyCode='" + convertedCurrencyCode + '\''
        + ", resultCode='" + resultCode + '\''
        + ", transactionHistoryId='" + transactionHistoryId + '\''
        + ", transactionId='" + transactionId + '\''
        + ", transactionResult='" + transactionResult + '\''
        + '}';
  }
}
