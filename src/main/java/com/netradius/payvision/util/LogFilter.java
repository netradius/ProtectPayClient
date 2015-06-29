package com.netradius.payvision.util;

import org.slf4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class filters log output to prevent passwords and credit card numbers form being logged.
 * @author Erik R. Jensen
 */
public class LogFilter {

	protected static final Pattern ccPattern = Pattern.compile("([0-9]{12,19})");

	protected Logger log;
	protected String[] filteredValues;

	public LogFilter(Logger log, String ... filteredValues) {
		this.log = log;
		this.filteredValues = filteredValues;
	}

	protected String lastFourDigits(String accountNumber) {
		if (StringUtils.hasText(accountNumber) && accountNumber.length() >= 4) {
			int idx = accountNumber.length() - 4;
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < idx; i++) {
				sb.append("*");
			}
			sb.append(accountNumber.substring(idx, accountNumber.length()));
			return sb.toString();
		}
		return accountNumber;
	}

	protected String filter(String s) {
		if (s == null || s.length() == 0) {
			return s;
		}
		if (filteredValues != null && filteredValues.length > 0) {
			for (String value : filteredValues) {
				s = s.replace(value, "<filtered>");
			}
		}
		Matcher m = ccPattern.matcher(s);
		while (m.find()) {
			String cc = m.group();
			String lastFourDigits = lastFourDigits(cc);
			s = s.replace(cc, lastFourDigits);
		}

		while (m.matches()) {
			String cc = m.group(1);
			String lastFourDigits = lastFourDigits(cc);
			s = s.replace(cc, lastFourDigits);
			m = ccPattern.matcher(s);
		}
		return s;
	}

	public void trace(String s) {
		log.trace(filter(s));
	}

	public void debug(String s) {
		log.debug(filter(s));
	}

	public void info(String s) {
		log.info(filter(s));
	}

	public void warn(String s) {
		log.warn(filter(s));
	}

	public void warn(String s, Throwable t) {
		log.warn(filter(s), t);
	}

	public void error(String s) {
		log.error(filter(s));
	}

	public void error(String s, Throwable t) {
		log.error(filter(s), t);
	}

	public boolean isDebugEnabled() {
		return log.isDebugEnabled();
	}

}
