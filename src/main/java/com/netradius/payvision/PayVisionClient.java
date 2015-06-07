package com.netradius.payvision;

import com.netradius.payvision.http.HttpClient;
import com.netradius.payvision.http.HttpURLConnectionClient;
import com.netradius.payvision.request.PayVisionQueryRequest;
import com.netradius.payvision.request.PayVisionRequest;
import com.netradius.payvision.response.PayVisionPaymentResponse;
import com.netradius.payvision.response.PayVisionQueryResponse;
import com.netradius.payvision.response.ResponseContentType;

import java.io.IOException;

/**
 *  @author Abhinav Nahar
 */
public class PayVisionClient {

	protected HttpClient httpClient;
	protected String transactUrl = "https://secure.nmi.com/api/transact.php";
	protected String queryUrl = "https://secure.nmi.com/api/query.php";

	public PayVisionClient(String username, String password) {
		this.httpClient = new HttpURLConnectionClient(username, password);
	}

	public PayVisionClient(String transactUrl, String queryUrl, String username, String password) {
		this(username, password);
		this.transactUrl = transactUrl;
		this.queryUrl = queryUrl;
	}

	public PayVisionPaymentResponse process(PayVisionRequest p) throws IOException {
		return httpClient.post(transactUrl, PayVisionPaymentResponse.class, p, ResponseContentType.JSON);
	}

	public PayVisionQueryResponse query(PayVisionQueryRequest p) throws IOException {
		return httpClient.post(queryUrl, PayVisionQueryResponse.class, p, ResponseContentType.XML);
	}
}
