package com.netradius.payvision.http;

/**
 * @author Abhinav Nahar
 */
import com.netradius.payvision.response.ResponseContentType;

import java.io.IOException;
import java.io.Serializable;

/**
 * Contract for all HttpClient implementations.
 *
 * @author Erik R. Jensen
 */
public interface HttpClient extends Serializable {

	<T> T post(String url, Class<T> responseTypeClazz, Object requestObject,
			ResponseContentType contentType) throws IOException;

}
