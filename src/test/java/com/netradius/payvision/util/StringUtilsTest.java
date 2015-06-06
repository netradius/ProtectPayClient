package com.netradius.payvision.util;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Erik R. Jensen
 */
public class StringUtilsTest {

	@Test
	public void testHasText() {
		assertFalse(StringUtils.hasText(null));
		assertFalse(StringUtils.hasText(""));
		assertFalse(StringUtils.hasText(" "));
		assertTrue(StringUtils.hasText("a "));
		assertTrue(StringUtils.hasText(" a"));
		assertTrue(StringUtils.hasText("a"));
	}
}
