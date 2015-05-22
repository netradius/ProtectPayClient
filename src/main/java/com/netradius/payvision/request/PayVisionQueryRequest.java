package com.netradius.payvision.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author Abhianv Nahar
 */
@Data
public class PayVisionQueryRequest extends PayVisionRequest {
	@JsonProperty("transaction_id")
	private String transactionId;
	private PayVisionQueryCondition condition;
	@JsonProperty("transaction_type")
	private PayVisionQueryTransactionType transactionType;//cc, ck
	@JsonProperty("action_type")
	private PayVisionQueryActionType actionType;
	@JsonProperty("order_id")
	private String orderId;
	@JsonProperty("email")
	private String email;
	@JsonProperty("cc_number")
	private String ccNumber;
	@JsonProperty("start_date")
	private String startDate; // (YYYYMMDDhhmmss)
	@JsonProperty("end_date")
	private String endDate; //(YYYYMMDDhhmmss)
	@JsonProperty("report_type")
	private PayVisionQueryReportType reportType;
	@JsonProperty("mobile_device_license")
	private String mobileDeviceLicense;
	@JsonProperty("mobile_device_nickname")
	private String mobileDeviceNickname;
	@JsonProperty("customer_vault_id")
	private String customerVaultId;
}
