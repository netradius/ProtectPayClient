package com.netradius.protectpay.oneoff;

import com.netradius.protectpay.ProtectPayBillingInfo;
import com.netradius.protectpay.ProtectPayBillingInfo.Country;
import com.netradius.protectpay.ProtectPayClient;
import com.netradius.protectpay.ProtectPayException;
import com.netradius.protectpay.ProtectPayPayer;
import com.netradius.protectpay.ProtectPayPaymentMethod;
import com.netradius.protectpay.ProtectPayPaymentMethod.DuplicateAction;
import com.netradius.protectpay.ProtectPayPaymentMethod.Type;
import com.netradius.protectpay.ProtectPayPayment;
import com.netradius.protectpay.ProtectPayPriorPayment;
import com.netradius.protectpay.ProtectPayPaymentResponse;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests the ProtectPay Client methods.
 *
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
      ProtectPayPayer criteria = new ProtectPayPayer();
      criteria.setAccountName("test");
      List<ProtectPayPayer> protectPayPayers = client.getPayers(criteria);
      for (ProtectPayPayer p : protectPayPayers) {
        log.debug("Deleting payer " + p);
        client.deletePayer(p.getPayerAccountId());
      }

      // Add new payer
      log.info("Adding payer");
      ProtectPayPayer protectPayPayer = new ProtectPayPayer();
      protectPayPayer.setEmailAddress("test@netradius.com");
      protectPayPayer.setExternalId1("ExternalTest1");
      protectPayPayer.setExternalId2("ExternalTest2");
      protectPayPayer.setAccountName("test");
      String payerAccountId = client.createPayer(protectPayPayer);
      assertEquals(payerAccountId, protectPayPayer.getPayerAccountId());

      // Update payer
      log.info("Updating payer");
      protectPayPayer.setExternalId1("ExternalTest1Modified");
      protectPayPayer.setExternalId2("ExternalTest2Modified");
      client.updatePayer(protectPayPayer);

      // Test get payers
      log.info("Getting payers");
      protectPayPayers = client.getPayers(criteria);
      assertTrue(protectPayPayers.size() == 1);
      ProtectPayPayer updated = protectPayPayers.get(0);
      assertEquals(protectPayPayer.getExternalId1(), updated.getExternalId1());
      assertEquals(protectPayPayer.getExternalId2(), updated.getExternalId2());

      // Create payment method
      log.info("Creating payment methods");
      ProtectPayPaymentMethod protectPayPaymentMethod = new ProtectPayPaymentMethod();
      protectPayPaymentMethod.setAccountName("John Doe");
      protectPayPaymentMethod.setPriority(1);
      protectPayPaymentMethod.setType(Type.Visa);
      protectPayPaymentMethod.setAccountNumber("4111111111111111");
      protectPayPaymentMethod.setDescription("Test Payment Method");
      protectPayPaymentMethod.setDuplicateAction(DuplicateAction.ERROR);
      protectPayPaymentMethod.setExpirationDate("0625");
      protectPayPaymentMethod.setPayerAccountId(protectPayPayer.getPayerAccountId());
      ProtectPayBillingInfo protectPayBillingInfo = new ProtectPayBillingInfo();
      protectPayBillingInfo.setAddress1("PO Box 463");
      protectPayBillingInfo.setCity("Draper");
      protectPayBillingInfo.setState("UT");
      protectPayBillingInfo.setCountry(Country.USA);
      protectPayBillingInfo.setZipCode("84020");
      protectPayBillingInfo.setEmailAddress("test@netradius.com");
      protectPayPaymentMethod.setBilling(protectPayBillingInfo);
      String paymentMethodId = client.createPaymentMethod(protectPayPaymentMethod);
      assertEquals(paymentMethodId, protectPayPaymentMethod.getPaymentMethodId());

      // Update payment method
      log.info("Updating payment method");
      protectPayPaymentMethod.setExpirationDate("0725");
      client.updatePaymentMethod(protectPayPaymentMethod);

      // Get payment method
      log.info("Getting payment method");
      ProtectPayPaymentMethod pm = client.getPaymentMethod(
          protectPayPaymentMethod.getPayerAccountId(), protectPayPaymentMethod
              .getPaymentMethodId());
      assertNotNull(pm);
      assertEquals(pm.getPaymentMethodId(), protectPayPaymentMethod.getPaymentMethodId());
      assertEquals("0725", pm.getExpirationDate());

      // Get all payment methods for payer
      log.info("Getting all payment methods");
      List<ProtectPayPaymentMethod> protectPayPaymentMethods = client.getPaymentMethods(
          protectPayPaymentMethod.getPayerAccountId());
      assertEquals(1, protectPayPaymentMethods.size());
      assertEquals(protectPayPaymentMethod.getPaymentMethodId(), protectPayPaymentMethods.get(0)
          .getPaymentMethodId());

      // Capture
      log.info("Executing auth");
      ProtectPayPayment protectPayPayment = new ProtectPayPayment();
      protectPayPayment.setAmount(100);
      protectPayPayment.setPayerAccountId(protectPayPaymentMethod.getPayerAccountId());
      protectPayPayment.setPaymentMethodId(protectPayPaymentMethod.getPaymentMethodId());
      protectPayPayment.setComment1("This is a test transaction");
      protectPayPayment.setComment2("This is a test transaction");
      protectPayPayment.setInputIpAddress("127.0.0.1");
      protectPayPayment.setInvoice(Integer.toString(rand.nextInt(100000) + 1));
      protectPayPayment.setCurrencyCode("USD");
      ProtectPayPaymentResponse response = client.auth(protectPayPayment, false);
      log.info("Auth Response: " + response.toString());
      assertEquals("00", response.getResultCode());
      log.info("Executing capture");
      ProtectPayPriorPayment protectPayPriorPayment = new ProtectPayPriorPayment();
      protectPayPriorPayment.setTransactionHistoryId(response.getTransactionHistoryId());
      protectPayPriorPayment.setOriginalTransactionId(response.getTransactionId());
      response = client.capture(protectPayPriorPayment, protectPayPayment.getAmount());
      log.info("Capture Response: " + response.toString());
      assertEquals("00", response.getResultCode());

      // Auth & Capture
      log.info("Executing authAndCapture");
      protectPayPayment.setInvoice(Integer.toString(rand.nextInt(100000) + 1));
      response = client.authAndCapture(protectPayPayment, false);
      log.info("Auth & Capture Response: " + response.toString());
      assertEquals("00", response.getResultCode());

      // Void
      protectPayPayment.setInvoice(Integer.toString(rand.nextInt(100000) + 1));
      response = client.auth(protectPayPayment, true);
      log.info("Executing Void");
      protectPayPriorPayment = new ProtectPayPriorPayment();
      protectPayPriorPayment.setOriginalTransactionId(response.getTransactionId());
      protectPayPriorPayment.setTransactionHistoryId(response.getTransactionHistoryId());
      protectPayPriorPayment.setComment1("test");
      protectPayPriorPayment.setComment2("test");
      response = client.voidPayment(protectPayPriorPayment);
      log.info("Void Response: " + response.toString());
      assertEquals("00", response.getResultCode());

      // Refund
      log.info("Executing Refund");
      protectPayPayment.setInvoice(Integer.toString(rand.nextInt(100000) + 1));
      response = client.authAndCapture(protectPayPayment, true);
      protectPayPriorPayment = new ProtectPayPriorPayment();
      protectPayPriorPayment.setOriginalTransactionId(response.getTransactionId());
      protectPayPriorPayment.setTransactionHistoryId(response.getTransactionHistoryId());
      protectPayPriorPayment.setComment1("test");
      protectPayPriorPayment.setComment2("test");
      response = client.refund(protectPayPriorPayment, 10);
      log.info("Refund Response: " + response.toString());
      assertEquals("00", response.getResultCode());

      // Credit
      log.info("Executing Credit");
      response = client.credit(protectPayPayment);
      log.info("Credit Response: " + response.toString());
      assertEquals("00", response.getResultCode());

      // Delete payment method
      client.deletePaymentMethod(protectPayPaymentMethod.getPayerAccountId(),
          protectPayPaymentMethod.getPaymentMethodId());
      protectPayPaymentMethods = client.getPaymentMethods(protectPayPaymentMethod
          .getPayerAccountId());
      assertEquals(0, protectPayPaymentMethods.size());

      // Delete payer
      protectPayPayers = client.getPayers(criteria);
      assertTrue(protectPayPayers.size() > 0);
      for (ProtectPayPayer p : protectPayPayers) {
        log.debug("Deleting payer " + p);
        client.deletePayer(p.getPayerAccountId());
      }

    } catch (ProtectPayException x) {
      log.error(x.toString(), x);
      assertTrue(false);
    }
  }
}
