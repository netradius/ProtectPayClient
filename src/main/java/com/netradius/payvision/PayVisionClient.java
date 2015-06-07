package com.netradius.payvision;

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

	private String url;
	private String username;
	private String password;

	public PayVisionClient(String url, String username, String password) {
		this.url = url;
		this.username = username;
		this.password = password;
	}

	public PayVisionPaymentResponse process(PayVisionRequest p) throws IOException {
		HttpURLConnectionClient connectionClient = new HttpURLConnectionClient(username, password);
		return connectionClient.post(url, PayVisionPaymentResponse.class, p, ResponseContentType.JSON);
	}

	public PayVisionQueryResponse query(PayVisionQueryRequest p) throws IOException {
		HttpURLConnectionClient connectionClient = new HttpURLConnectionClient(username, password);
		return connectionClient.post(url, PayVisionQueryResponse.class, p, ResponseContentType.XML);
	}
}
