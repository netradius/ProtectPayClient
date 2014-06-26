package com.netradius.protectpay;

import java.io.Serializable;

/**
 * Holds prior payment data.
 *
 * @author Erik R. Jensen
 */
public class PriorPayment implements Serializable {

	private static final long serialVersionUID = 3678260477933994506L;

	private String originalTransactionId;
	private Long transactionHistoryId;
	private Long merchantProfileId;
	private String comment1;
	private String comment2;

	/**
	 * Returns the original transaction ID. This is the gateway's identifier.
	 *
	 * @return the original transaction ID
	 */
	public String getOriginalTransactionId() {
		return originalTransactionId;
	}

	/**
	 * Sets the original transaction ID. This it the gateway's identifier.
	 *
	 * param originalTransactionId the original transaction ID
	 */
	public void setOriginalTransactionId(String originalTransactionId) {
		this.originalTransactionId = originalTransactionId;
	}

	/**
	 * Returns the transaction history ID. This is ProtectPay's identifier.
	 *
	 * @return the transaction history ID
	 */
	public Long getTransactionHistoryId() {
		return transactionHistoryId;
	}

	/**
	 * Sets the transaction history ID. This is ProtectPay's identifier.
	 *
	 * @param transactionHistoryId the transaction history ID
	 */
	public void setTransactionHistoryId(Long transactionHistoryId) {
		this.transactionHistoryId = transactionHistoryId;
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
	 * Sets the merchant profile ID.
	 *
	 * @param merchantProfileId the merchant profile ID
	 */
	public void setMerchantProfileId(Long merchantProfileId) {
		this.merchantProfileId = merchantProfileId;
	}

	/**
	 * Returns the first comment.
	 *
	 * @return the first comment
	 */
	public String getComment1() {
		return comment1;
	}

	/**
	 * Sets the first comment.
	 *
	 * @param comment1 the first comment
	 */
	public void setComment1(String comment1) {
		this.comment1 = comment1;
	}

	/**
	 * Returns the second comment.
	 *
	 * @return the second comment
	 */
	public String getComment2() {
		return comment2;
	}

	/**
	 * Sets the second comment.
	 *
	 * @param comment2 the second comment
	 */
	public void setComment2(String comment2) {
		this.comment2 = comment2;
	}
}
