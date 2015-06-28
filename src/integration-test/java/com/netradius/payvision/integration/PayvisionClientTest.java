package com.netradius.payvision.integration;

import com.netradius.payvision.PayvisionClient;
import com.netradius.payvision.request.*;
import com.netradius.payvision.response.PayvisionPaymentResponse;
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
public class PayvisionClientTest {

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
		PayvisionPaymentResponse response = doCapture();
		PayvisionVoidRequest req = new PayvisionVoidRequest()
				.setTransactionId(response.getTransactionId());
		response = client.process(req);
		Assert.assertEquals(PayvisionResponseType.APPROVED, response.getResponseType());
	}

	@Test
	public void testRefund() throws IOException {
		PayvisionPaymentResponse response = doCapture();
		PayvisionRefundRequest req = new PayvisionRefundRequest()
				.setTransactionId(response.getTransactionId())
				.setAmount(new BigDecimal("50"));
		response = client.process(req);
		Assert.assertEquals(PayvisionResponseType.APPROVED, response.getResponseType());
	}

	@Test
	public void testCredit() throws IOException {
		PayvisionPaymentResponse response = doCapture();
		PayvisionCreditRequest req = new PayvisionCreditRequest()
				.setTransactionId(response.getTransactionId())
				.setAmount(new BigDecimal("50"));
		response = client.process(req);
		Assert.assertEquals(PayvisionResponseType.APPROVED, response.getResponseType());
	}

	@Test
	public void testValidate() throws IOException {
		PayvisionValidateRequest request = new PayvisionValidateRequest()
				.setCreditCard(creditCard())
				.setBillingInfo(billingInfo())
				.setBillingInfo(billingInfo());
		PayvisionPaymentResponse response = client.process(request);
		Assert.assertEquals(PayvisionResponseType.APPROVED, response.getResponseType());
	}


	@Test
	public void testSale() throws IOException {
		String orderID = "TEST" + RANDOM.nextInt();
		int amount = Math.abs(RANDOM.nextInt() % 1000);
		PayvisionSaleRequest req = new PayvisionSaleRequest()
				.setAmount(new BigDecimal(amount))
				.setCurrency("USD")
				.setOrderInfo(new OrderInfo()
						.setOrderId(orderID))
				.setCreditCard(creditCard())
				.setBillingInfo(billingInfo());
		PayvisionPaymentResponse response = client.process(req);
		Assert.assertEquals(PayvisionResponseType.APPROVED, response.getResponseType());
	}

	@Test
	public void testUpdate() throws IOException {
		String orderID = "TEST" + RANDOM.nextInt();
		int amount = Math.abs(RANDOM.nextInt()%1000);
		PayvisionSaleRequest req = new PayvisionSaleRequest()
				.setAmount(new BigDecimal(amount))
				.setCurrency("USD")
				.setOrderInfo(new OrderInfo()
						.setOrderId(orderID))
				.setCreditCard(creditCard())
				.setBillingInfo(billingInfo());

		PayvisionPaymentResponse response = client.process(req);
		Assert.assertEquals(PayvisionResponseType.APPROVED, response.getResponseType());

		PayvisionUpdateRequest updateRequest = new PayvisionUpdateRequest()
				.setFirstName("test")
				.setLastName("last")
				.setTransactionId(response.getTransactionId())
				.setDiscountAmount(new BigDecimal("10"));
		response = client.process(updateRequest);
		Assert.assertEquals(PayvisionResponseType.APPROVED, response.getResponseType());
	}

	@Test
	public void testQuery() throws IOException {
		RANDOM.nextInt();
		PayvisionPaymentResponse response = doCapture();
		PayvisionQueryRequest req = new PayvisionQueryRequest()
				.setTransactionId(response.getTransactionId());
	//	req.setActionType(PayVisionQueryActionType.AUTH);
		PayvisionQueryResponse res = client.query(req);
		Assert.assertNotNull(res.getTransaction());
		Assert.assertEquals(2, res.getTransaction().getAction().length);
	}

	private PayvisionPaymentResponse doCapture() throws IOException {
		String orderID = "TEST" + RANDOM.nextInt();
		PayvisionPaymentResponse response = doAuth(orderID);
		PayvisionCaptureRequest req = new PayvisionCaptureRequest()
				.setAmount(new BigDecimal("10.00"))
				.setTransactionId(response.getTransactionId());

		response = client.process(req);
		Assert.assertEquals(PayvisionResponseType.APPROVED, response.getResponseType());
		return response;
	}

	private PayvisionPaymentResponse doAuth(String orderId) throws IOException {
		int amount = Math.abs(RANDOM.nextInt()%1000);
		PayvisionAuthRequest req = new PayvisionAuthRequest()
				.setAmount(new BigDecimal(amount))
				.setCurrency("USD")
				.setOrderInfo(new OrderInfo()
						.setOrderId(orderId))
				.setCreditCard(creditCard())
				.setBillingInfo(billingInfo());
		//req.setPaymentDescriptor(new PaymentDescriptor());
		PayvisionPaymentResponse response = client.process(req);
		Assert.assertEquals(PayvisionResponseType.APPROVED, response.getResponseType());
		return response;
	}

	private CreditCard creditCard() {
		return new CreditCard()
				.setCcnumber("4111111111111111")
				.setExpirationDate("0916")
				.setCvv("999");
	}

	private BillingInfo billingInfo() {
		return new BillingInfo()
				.setAddress1("test adddress1")
				.setAddress2("test adddress2")
				.setCity("Test c123213213oty")
				.setCountry("US")
		//b.setEmail();
				.setState("UT")
				.setZip("84121");
	}
}