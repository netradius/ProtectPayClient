package com.netradius.protectpay;

import java.io.Serializable;

/**
 * Holds Payer override values. These values will override the values on a
 * payment method when used to make a payment.
 *
 * @author Abhijeet Kale
 */
public class PayerOverride implements Serializable {

  private String inputIpAddress;

  /**
   * Gets the InputAddress.
   *
   * @return the input IP Address
   */
  public String getInputIpAddress() {
    return inputIpAddress;
  }

  /**
   * Sets the Input IP Address.
   *
   * @param inputIpAddress the IP Address information
   */
  public void setInputIpAddress(String inputIpAddress) {
    this.inputIpAddress = inputIpAddress;
  }

  @Override
  public String toString() {
    return "PayerOverride{"
        + "inputIpAddress='" + inputIpAddress + '\''
        + '}';
  }
}
