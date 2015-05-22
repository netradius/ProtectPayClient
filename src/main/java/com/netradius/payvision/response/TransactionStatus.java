package com.netradius.payvision.response;

/**
 * @author Abhinav Nahar
 */
public enum TransactionStatus {
	FAILED("3"), APPROVED("1"), DECLINED("2");

	private String id;
	TransactionStatus(String id) {
		this.id = id;
	}

	public static TransactionStatus getTransactionStatusById(String id) {
		switch (id) {
			case "1":
				return APPROVED;
			case "2":
				return DECLINED;
			case "3":
				return FAILED;
			default:
				return null;
		}
	}
}
