package com.netradius.payvision;

import com.netradius.payvision.request.*;
import com.netradius.payvision.response.PayVisionPaymentResponse;
import com.netradius.payvision.response.PayVisionQueryResponse;
import com.netradius.payvision.response.TransactionStatus;
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
	private static PayVisionClient client;

	@BeforeClass
	public static void init() {
		TestConfig config = new TestConfig();
		client = new PayVisionClient(config.getUsername(), config.getPassword());
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
		PayVisionPaymentResponse response = doCapture();
		PavVisionVoidRequest req = new PavVisionVoidRequest();
		req.setTransactionId(response.getTransactionId());
		response = client.process(req);
		Assert.assertEquals(TransactionStatus.APPROVED, response.getTransactionStatus());
	}

	@Test
	public void testRefund() throws IOException {
		PayVisionPaymentResponse response = doCapture();
		PayVisionRefundRequest req = new PayVisionRefundRequest();
		req.setTransactionId(response.getTransactionId());
		req.setAmount(new BigDecimal("50"));
		response = client.process(req);
		Assert.assertEquals(TransactionStatus.APPROVED, response.getTransactionStatus());
	}

	@Test
	public void testCredit() throws IOException {
		PayVisionPaymentResponse response = doCapture();
		PayVisionCreditRequest req = new PayVisionCreditRequest();
		req.setTransactionId(response.getTransactionId());
		req.setAmount(new BigDecimal("50"));
		response = client.process(req);
		Assert.assertEquals(TransactionStatus.APPROVED, response.getTransactionStatus());
	}

	@Test
	public void testValidate() throws IOException {
		PayVisionValidateRequest request = new PayVisionValidateRequest();
		request.setCreditCard(creditCard());
		request.setBillingInfo(billingInfo());
		request.setBillingInfo(billingInfo());
		PayVisionPaymentResponse response = client.process(request);
		Assert.assertEquals(TransactionStatus.APPROVED, response.getTransactionStatus());
	}


	@Test
	public void testSale() throws IOException {
		String orderID = "TEST" + RANDOM.nextInt();
		PayVisionSaleRequest req = new PayVisionSaleRequest();
		int amount = Math.abs(RANDOM.nextInt() % 1000);
		req.setAmount(new BigDecimal(amount));
		req.setCurrency("USD");
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setOrderId(orderID);
		req.setOrderInfo(orderInfo);
		req.setCreditCard(creditCard());
		req.setBillingInfo(billingInfo());
		PayVisionPaymentResponse response = client.process(req);
		Assert.assertEquals(TransactionStatus.APPROVED, response.getTransactionStatus());
	}

	@Test
	public void testUpdate() throws IOException {
		String orderID = "TEST" + RANDOM.nextInt();
		PayVisionSaleRequest req = new PayVisionSaleRequest();
		req.setAmount(new BigDecimal("152.00"));
		req.setCurrency("USD");
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setOrderId(orderID);
		req.setOrderInfo(orderInfo);
		req.setCreditCard(creditCard());
		req.setBillingInfo(billingInfo());

		PayVisionPaymentResponse response = client.process(req);
		Assert.assertEquals(TransactionStatus.APPROVED, response.getTransactionStatus());

		PayVisionUpdateRequest updateRequest = new PayVisionUpdateRequest();
		updateRequest.setFirstName("test");
		updateRequest.setLastName("last");
		updateRequest.setTransactionId(response.getTransactionId());
		updateRequest.setDiscountAmount(new BigDecimal("10"));
		response = client.process(updateRequest);
		Assert.assertEquals(TransactionStatus.APPROVED, response.getTransactionStatus());
	}

	@Test
	public void testQuery() throws IOException {
		RANDOM.nextInt();
		PayVisionPaymentResponse response = doCapture();
		PayVisionQueryRequest req = new PayVisionQueryRequest();
		req.setTransactionId(response.getTransactionId());
	//	req.setActionType(PayVisionQueryActionType.AUTH);
		PayVisionQueryResponse res = client.query(req);
		Assert.assertNotNull(res.getTransaction());
		Assert.assertEquals(2, res.getTransaction().getAction().length);
	}

	private PayVisionPaymentResponse doCapture() throws IOException {
		String orderID = "TEST" + RANDOM.nextInt();
		PayVisionPaymentResponse response = doAuth(orderID);
		PayVisionCaptureRequest payVisionPayment = new PayVisionCaptureRequest();
		payVisionPayment.setAmount(new BigDecimal("10.00"));
		payVisionPayment.setTransactionId(response.getTransactionId());

		response = client.process(payVisionPayment);
		Assert.assertEquals(TransactionStatus.APPROVED, response.getTransactionStatus());
		return response;
	}

	private PayVisionPaymentResponse doAuth(String orderId) throws IOException {
		int amount = Math.abs(RANDOM.nextInt()%1000);
		PayVisionAuthRequest payVisionPayment = new PayVisionAuthRequest();
		payVisionPayment.setAmount(new BigDecimal(amount));
		payVisionPayment.setCurrency("USD");
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setOrderId(orderId);
		payVisionPayment.setOrderInfo(orderInfo);
		payVisionPayment.setCreditCard(creditCard());
		payVisionPayment.setBillingInfo(billingInfo());
		//payVisionPayment.setPaymentDescriptor(new PaymentDescriptor());
		PayVisionPaymentResponse response = client.process(payVisionPayment);
		Assert.assertEquals(TransactionStatus.APPROVED, response.getTransactionStatus());
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