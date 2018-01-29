package com.netradius.protectpay;

import java.io.Serializable;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;

/**
 * Holds ACH override values. These values will override the values on a
 * payment method when used to make a payment.
 *
 * @author Erik R. Jensen
 */
public class ACHOverride implements Serializable {

  private static final long serialVersionUID = -1674203392268641681L;

  public enum Type {
    Checking,
    Savings
  }

  public enum SecCode {
    WEB,
    TEL,
    PPO,
    CCD
  }

  private Type bankAccountType;

  @NotNull
  private SecCode secCode;

  /**
   * Returns the type of bank account.
   *
   * @return the bank account type
   */
  public Type getBankAccountType() {
    return bankAccountType;
  }

  /**
   * Sets the type of bank account. This field is optional if the type is defined on the payment
   * method.
   *
   * @param bankAccountType the bank account type
   */
  public void setBankAccountType(Type bankAccountType) {
    this.bankAccountType = bankAccountType;
  }

  /**
   * Returns the SEC code.
   *
   * @return the SEC code
   */
  public SecCode getSecCode() {
    return secCode;
  }

  /**
   * Sets the SEC code. This field is required.
   *
   * @param secCode the SEC code
   */
  public void setSecCode(SecCode secCode) {
    this.secCode = secCode;
  }

  @Override
  public String toString() {
    return "AchOverride{"
        + "bankAccountType=" + bankAccountType
        + ", secCode='" + secCode + '\''
        + '}';
  }

  /**
   * Performs JSR 303 validation on the object.
   *
   * @return constraint violations
   */
  public Set<ConstraintViolation<ACHOverride>> validate() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();
    return validator.validate(this, getClass());
  }
}
