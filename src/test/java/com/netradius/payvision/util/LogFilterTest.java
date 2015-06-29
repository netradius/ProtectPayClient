package com.netradius.payvision.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Erik R. Jensen
 */
@Slf4j
public class LogFilterTest {

	@Test
	public void testLastFourDigits() {
		LogFilter logf = new LogFilter(log);
		String s = logf.lastFourDigits("4111111111111111");
		assertEquals("1111", s);
	}

	@Test
	public void testFilter() {
		LogFilter logf = new LogFilter(log, "test");
		String filtered = logf.filter("Credit card 4111111111111111 is a test card.");
		assertEquals("Credit card 1111 is a <filtered> card.", filtered);
	}
}
