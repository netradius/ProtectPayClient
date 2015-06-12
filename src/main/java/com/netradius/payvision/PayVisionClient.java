package com.netradius.payvision;

import com.netradius.payvision.http.HttpClient;
import com.netradius.payvision.http.HttpURLConnectionClient;
import com.netradius.payvision.request.PayvisionQueryRequest;
import com.netradius.payvision.request.PayvisionRequest;
import com.netradius.payvision.response.PayvisionPaymentResponse;
import com.netradius.payvision.response.PayvisionQueryResponse;
import com.netradius.payvision.response.ResponseContentType;

import java.io.IOException;

/**
 *  @author Abhinav Nahar
 */
public class PayvisionClient {

	protected HttpClient httpClient;
	protected String transactUrl = "https://secure.nmi.com/api/transact.php";
	protected String queryUrl = "https://secure.nmi.com/api/query.php";

	public PayvisionClient(String username, String password) {
		this.httpClient = new HttpURLConnectionClient(username, password);
	}

	public PayvisionClient(String transactUrl, String queryUrl, String username, String password) {
		this(username, password);
		this.transactUrl = transactUrl;
		this.queryUrl = queryUrl;
	}

	public PayvisionPaymentResponse process(PayvisionRequest p) throws IOException {
		return httpClient.post(transactUrl, PayvisionPaymentResponse.class, p, ResponseContentType.JSON);
	}

	public PayvisionQueryResponse query(PayvisionQueryRequest p) throws IOException {
		return httpClient.post(queryUrl, PayvisionQueryResponse.class, p, ResponseContentType.XML);
	}
}
