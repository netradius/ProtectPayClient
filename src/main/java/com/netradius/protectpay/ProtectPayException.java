package com.netradius.protectpay;

/**
 * Thrown when an error is returned from ProtectPay. This exception encapsulates the result code, value and
 * message returned from ProtectPay.
 *
 * @author Erik R. Jensen
 */
public class ProtectPayException extends Exception {

	private static final long serialVersionUID = -1395851064558847814L;

	private String resultCode;
	private String resultValue;
	private String resultMessage;

	public ProtectPayException(String resultCode, String resultValue, String resultMessage) {
		super(resultMessage);
		this.resultCode = resultCode;
		this.resultValue = resultValue;
		this.resultMessage = resultMessage;
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
	 * Returns the result value.
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

	@Override
	public String toString() {
		return "ProtectPayException{" +
				"resultCode='" + resultCode + '\'' +
				", resultValue='" + resultValue + '\'' +
				", resultMessage='" + resultMessage + '\'' +
				'}';
	}
}
