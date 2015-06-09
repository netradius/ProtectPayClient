package com.netradius.payvision;

import com.netradius.payvision.request.*;
import com.netradius.payvision.response.PaycisionPaymentResponse;
import com.netradius.payvision.response.PayvisionQueryResponse;
import com.netradius.payvision.response.PayvisionResponseType;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Random;

/**
 * @author Abhinav Nahar
 */
public class PatVisionClientTest {

	public static final Random RANDOM = new Random();
	private static PayvisionClient client;

	@BeforeClass
	public static void init() {
		TestConfig config = new TestConfig();
		client = new PayvisionClient(config.getUsername(), config.getPassword());
	}

	@Test
	public void testAuth() throws IOException {
		doAuth("1234");
	}

	@Test
	public void testCapture() throws IOException {
		doCapture();
	}

	@Test
	public void testVoid() throws IOException {
		PaycisionPaymentResponse response = doCapture();
		PavvisionVoidRequest req = new PavvisionVoidRequest();
		req.setTransactionId(response.getTransactionId());
		response = client.process(req);
		Assert.assertEquals(PayvisionResponseType.APPROVED, response.getResponseType());
	}

	@Test
	public void testRefund() throws IOException {
		PaycisionPaymentResponse response = doCapture();
		PayvisionRefundRequest req = new PayvisionRefundRequest();
		req.setTransactionId(response.getTransactionId());
		req.setAmount(new BigDecimal("50"));
		response = client.process(req);
		Assert.assertEquals(PayvisionResponseType.APPROVED, response.getResponseType());
	}

	@Test
	public void testCredit() throws IOException {
		PaycisionPaymentResponse response = doCapture();
		PayvisionCreditRequest req = new PayvisionCreditRequest();
		req.setTransactionId(response.getTransactionId());
		req.setAmount(new BigDecimal("50"));
		response = client.process(req);
		Assert.assertEquals(PayvisionResponseType.APPROVED, response.getResponseType());
	}

	@Test
	public void testValidate() throws IOException {
		PayvisionValidateRequest request = new PayvisionValidateRequest();
		request.setCreditCard(creditCard());
		request.setBillingInfo(billingInfo());
		request.setBillingInfo(billingInfo());
		PaycisionPaymentResponse response = client.process(request);
		Assert.assertEquals(PayvisionResponseType.APPROVED, response.getResponseType());
	}


	@Test
	public void testSale() throws IOException {
		String orderID = "TEST" + RANDOM.nextInt();
		PayvisionSaleRequest req = new PayvisionSaleRequest();
		int amount = Math.abs(RANDOM.nextInt() % 1000);
		req.setAmount(new BigDecimal(amount));
		req.setCurrency("USD");
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setOrderId(orderID);
		req.setOrderInfo(orderInfo);
		req.setCreditCard(creditCard());
		req.setBillingInfo(billingInfo());
		PaycisionPaymentResponse response = client.process(req);
		Assert.assertEquals(PayvisionResponseType.APPROVED, response.getResponseType());
	}

	@Test
	public void testUpdate() throws IOException {
		String orderID = "TEST" + RANDOM.nextInt();
		int amount = Math.abs(RANDOM.nextInt()%1000);
		PayvisionSaleRequest req = new PayvisionSaleRequest();
		req.setAmount(new BigDecimal(amount));
		req.setCurrency("USD");
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setOrderId(orderID);
		req.setOrderInfo(orderInfo);
		req.setCreditCard(creditCard());
		req.setBillingInfo(billingInfo());

		PaycisionPaymentResponse response = client.process(req);
		Assert.assertEquals(PayvisionResponseType.APPROVED, response.getResponseType());

		PayvisionUpdateRequest updateRequest = new PayvisionUpdateRequest();
		updateRequest.setFirstName("test");
		updateRequest.setLastName("last");
		updateRequest.setTransactionId(response.getTransactionId());
		updateRequest.setDiscountAmount(new BigDecimal("10"));
		response = client.process(updateRequest);
		Assert.assertEquals(PayvisionResponseType.APPROVED, response.getResponseType());
	}

	@Test
	public void testQuery() throws IOException {
		RANDOM.nextInt();
		PaycisionPaymentResponse response = doCapture();
		PayvisionQueryRequest req = new PayvisionQueryRequest();
		req.setTransactionId(response.getTransactionId());
	//	req.setActionType(PayVisionQueryActionType.AUTH);
		PayvisionQueryResponse res = client.query(req);
		Assert.assertNotNull(res.getTransaction());
		Assert.assertEquals(2, res.getTransaction().getAction().length);
	}

	private PaycisionPaymentResponse doCapture() throws IOException {
		String orderID = "TEST" + RANDOM.nextInt();
		PaycisionPaymentResponse response = doAuth(orderID);
		PayvisionCaptureRequest payVisionPayment = new PayvisionCaptureRequest();
		payVisionPayment.setAmount(new BigDecimal("10.00"));
		payVisionPayment.setTransactionId(response.getTransactionId());

		response = client.process(payVisionPayment);
		Assert.assertEquals(PayvisionResponseType.APPROVED, response.getResponseType());
		return response;
	}

	private PaycisionPaymentResponse doAuth(String orderId) throws IOException {
		int amount = Math.abs(RANDOM.nextInt()%1000);
		PayvisionAuthRequest payVisionPayment = new PayvisionAuthRequest();
		payVisionPayment.setAmount(new BigDecimal(amount));
		payVisionPayment.setCurrency("USD");
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setOrderId(orderId);
		payVisionPayment.setOrderInfo(orderInfo);
		payVisionPayment.setCreditCard(creditCard());
		payVisionPayment.setBillingInfo(billingInfo());
		//payVisionPayment.setPaymentDescriptor(new PaymentDescriptor());
		PaycisionPaymentResponse response = client.process(payVisionPayment);
		Assert.assertEquals(PayvisionResponseType.APPROVED, response.getResponseType());
		return response;
	}

	private CreditCard creditCard() {
		CreditCard cc = new CreditCard();
		cc.setCcnumber("4111111111111111");
		cc.setExpirationDate("0916");
		cc.setCvv("999");
		return cc;
	}

	private BillingInfo billingInfo() {
		BillingInfo b =  new BillingInfo();
		b.setAddress1("test adddress1");
		b.setAddress2("test adddress2");
		b.setCity("Test c123213213oty");
		b.setCountry("US");
		//b.setEmail();
		b.setState("UT");
		b.setZip("84121");
		return b;
	}
}