package com.netradius.protectpay.oneoff;

import com.netradius.protectpay.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * @author Erik R. Jensen
 */
public class ProtectPayClientTest {

	private static final Logger log = LoggerFactory.getLogger(ProtectPayClientTest.class);

	@Test
	public void test() throws ProtectPayException {
		Random rand = new Random();
		ProtectPayClient client = new ProtectPayClient(
				ProtectPayClient.TESTING_URL,
				"PUT AUTHENTICATION TOKEN HERE",
				"PUT BILLER ACCOUNT ID HERE");

		try {

			// Purge any payers with the same account name we are about to use
			log.info("Purging previous payers named test");
			Payer criteria = new Payer();
			criteria.setAccountName("test");
			List<Payer> payers = client.getPayers(criteria);
			for (Payer p : payers) {
				log.debug("Deleting payer " + p);
				client.deletePayer(p.getPayerAccountId());
			}

			// Add new payer
			log.info("Adding payer");
			Payer payer = new Payer();
			payer.setEmailAddress("test@netradius.com");
			payer.setExternalId1("ExternalTest1");
			payer.setExternalId2("ExternalTest2");
			payer.setAccountName("test");
			String payerAccountId = client.createPayer(payer);
			assertEquals(payerAccountId, payer.getPayerAccountId());

			// Update payer
			log.info("Updating payer");
			payer.setExternalId1("ExternalTest1Modified");
			payer.setExternalId2("ExternalTest2Modified");
			client.updatePayer(payer);

			// Test get payers
			log.info("Getting payers");
			payers = client.getPayers(criteria);
			assertTrue(payers.size() == 1);
			Payer updated = payers.get(0);
			assertEquals(payer.getExternalId1(), updated.getExternalId1());
			assertEquals(payer.getExternalId2(), updated.getExternalId2());

			// Create payment method
			log.info("Creating payment methods");
			PaymentMethod paymentMethod = new PaymentMethod();
			paymentMethod.setAccountName("John Doe");
			paymentMethod.setPriority(1);
			paymentMethod.setType(PaymentMethod.Type.Visa);
			paymentMethod.setAccountNumber("4111111111111111");
			paymentMethod.setDescription("Test Payment Method");
			paymentMethod.setDuplicateAction(PaymentMethod.DuplicateAction.ERROR);
			paymentMethod.setExpirationDate("0625");
			paymentMethod.setPayerAccountId(payer.getPayerAccountId());
			BillingInfo billingInfo = new BillingInfo();
			billingInfo.setAddress1("PO Box 463");
			billingInfo.setCity("Draper");
			billingInfo.setState("UT");
			billingInfo.setCountry(BillingInfo.Country.USA);
			billingInfo.setZipCode("84020");
			billingInfo.setEmailAddress("test@netradius.com");
			paymentMethod.setBilling(billingInfo);
			String paymentMethodId = client.createPaymentMethod(paymentMethod);
			assertEquals(paymentMethodId, paymentMethod.getPaymentMethodId());

			// Update payment method
			log.info("Updating payment method");
			paymentMethod.setExpirationDate("0725");
			client.updatePaymentMethod(paymentMethod);

			// Get payment method
			log.info("Getting payment method");
			PaymentMethod pm = client.getPaymentMethod(
					paymentMethod.getPayerAccountId(), paymentMethod.getPaymentMethodId());
			assertNotNull(pm);
			assertEquals(pm.getPaymentMethodId(), paymentMethod.getPaymentMethodId());
			assertEquals("0725", pm.getExpirationDate());

			// Get all payment methods for payer
			log.info("Getting all payment methods");
			List<PaymentMethod> paymentMethods = client.getPaymentMethods(paymentMethod.getPayerAccountId());
			assertEquals(1, paymentMethods.size());
			assertEquals(paymentMethod.getPaymentMethodId(), paymentMethods.get(0).getPaymentMethodId());

			// Capture
			log.info("Executing auth");
			Payment payment = new Payment();
			payment.setAmount(100);
			payment.setPayerAccountId(paymentMethod.getPayerAccountId());
			payment.setPaymentMethodId(paymentMethod.getPaymentMethodId());
			payment.setComment1("This is a test transaction");
			payment.setComment2("This is a test transaction");
			payment.setInputIpAddress("127.0.0.1");
			payment.setInvoice(Integer.toString(rand.nextInt(100000) + 1));
			payment.setCurrencyCode("USD");
			PaymentResponse response = client.auth(payment, false);
			log.info("Auth Response: " + response.toString());
			assertEquals("00", response.getResultCode());
			log.info("Executing capture");
			PriorPayment priorPayment = new PriorPayment();
			priorPayment.setTransactionHistoryId(response.getTransactionHistoryId());
			priorPayment.setOriginalTransactionId(response.getTransactionId());
			response = client.capture(priorPayment, payment.getAmount());
			log.info("Capture Response: " + response.toString());
			assertEquals("00", response.getResultCode());

			// Auth & Capture
			log.info("Executing authAndCapture");
			payment.setInvoice(Integer.toString(rand.nextInt(100000) + 1));
			response = client.authAndCapture(payment, false);
			log.info("Auth & Capture Response: " + response.toString());
			assertEquals("00", response.getResultCode());

			// Void
			payment.setInvoice(Integer.toString(rand.nextInt(100000) + 1));
			response = client.auth(payment, true);
			log.info("Executing Void");
			priorPayment = new PriorPayment();
			priorPayment.setOriginalTransactionId(response.getTransactionId());
			priorPayment.setTransactionHistoryId(response.getTransactionHistoryId());
			priorPayment.setComment1("test");
			priorPayment.setComment2("test");
			response = client.voidPayment(priorPayment);
			log.info("Void Response: " + response.toString());
			assertEquals("00", response.getResultCode());

			// Refund
			log.info("Executing Refund");
			payment.setInvoice(Integer.toString(rand.nextInt(100000) + 1));
			response = client.authAndCapture(payment, true);
			priorPayment = new PriorPayment();
			priorPayment.setOriginalTransactionId(response.getTransactionId());
			priorPayment.setTransactionHistoryId(response.getTransactionHistoryId());
			priorPayment.setComment1("test");
			priorPayment.setComment2("test");
			response = client.refund(priorPayment, 10);
			log.info("Refund Response: " + response.toString());
			assertEquals("00", response.getResultCode());

			// Credit
			log.info("Executing Credit");
			response = client.credit(payment);
			log.info("Credit Response: " + response.toString());
			assertEquals("00", response.getResultCode());

			// Delete payment method
			client.deletePaymentMethod(paymentMethod.getPayerAccountId(), paymentMethod.getPaymentMethodId());
			paymentMethods = client.getPaymentMethods(paymentMethod.getPayerAccountId());
			assertEquals(0, paymentMethods.size());

			// Delete payer
			payers = client.getPayers(criteria);
			assertTrue(payers.size() > 0);
			for (Payer p : payers) {
				log.debug("Deleting payer " + p);
				client.deletePayer(p.getPayerAccountId());
			}

		} catch (ProtectPayException x) {
			log.error(x.toString(), x);
			assertTrue(false);
		}
	}
}
