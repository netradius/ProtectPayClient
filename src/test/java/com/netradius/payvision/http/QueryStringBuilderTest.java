package com.netradius.payvision.http;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author Erik R. Jensen
 */
public class QueryStringBuilderTest {

	@Test
	public void testToQueryString() {
		QueryStringBuilder qb = new QueryStringBuilder()
				.add("test1", "test1")
				.add("test2", "test2");
		String queryString = qb.toQueryString();
		assertThat(queryString, equalTo("test1=test1&test2=test2"));
	}

	@Test
	public void testEncoddedQueryString1() {
		QueryStringBuilder qb = new QueryStringBuilder()
				.add("test1", "m&m")
				.add("test2", "x=y");
		String queryString = qb.toQueryString();
		assertThat(queryString, equalTo("test1=m%26m&test2=x%3Dy"));
	}
}
