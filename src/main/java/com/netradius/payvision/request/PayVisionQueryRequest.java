package com.netradius.payvision.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author Abhianv Nahar
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PayvisionQueryRequest extends PayvisionRequest {

	@JsonProperty("transaction_id")
	private String transactionId;

	private PayvisionQueryCondition condition;

	@JsonProperty("transaction_type")
	private PayvisionQueryTransactionType transactionType;//cc, ck

	@JsonProperty("action_type")
	private PayvisionQueryActionType actionType;

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
	private PayvisionQueryReportType reportType;

	@JsonProperty("mobile_device_license")
	private String mobileDeviceLicense;

	@JsonProperty("mobile_device_nickname")
	private String mobileDeviceNickname;

	@JsonProperty("customer_vault_id")
	private String customerVaultId;
}
