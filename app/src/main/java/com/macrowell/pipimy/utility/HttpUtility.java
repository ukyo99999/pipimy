package com.macrowell.pipimy.utility;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

public class HttpUtility {

	// Timeout (ms) for establishing a connection.
	private static final int DEFAULT_CONNECT_TIMEOUT_MILLIS = 30 * 1000;

	// Timeout (ms) for read operations on connections.
	private static final int DEFAULT_READ_TIMEOUT_MILLIS = 30 * 1000;

	// Timeout (ms) for obtaining a connection from the connection pool.
	private static final int DEFAULT_GET_CONNECTION_FROM_POOL_TIMEOUT_MILLIS = 30 * 1000;

	public static String execute(HttpUriRequest request) throws Exception {
		DefaultHttpClient httpClient = null;
		String result = null;

		try {
			httpClient = createHttpClient();
			HttpResponse httpResponse = httpClient.execute(request);

			// Header[] headers = httpResponse.getAllHeaders();
			//
			// for (Header header : headers) {
			// System.out.println("Key : " + header.getName() + " ,Value : "
			// + header.getValue());
			// }

			HttpEntity entity = httpResponse.getEntity();
			InputStream instream = entity.getContent();

			char[] buffer = new char[1024];
			int flag = -1;
			StringWriter sw = new StringWriter();
			InputStreamReader in = new InputStreamReader(instream, "UTF-8");

			while ((flag = in.read(buffer)) > 0) {
				sw.write(buffer, 0, flag);
			}

			result = sw.toString();

		} catch (Exception e) {
			throw e;
		}

		return result;
	}

	public static String post(String url, Map<String, String> params)
			throws Exception {

		HttpPost httpPost = new HttpPost(url);
		setEntity(httpPost, params);

		return execute(httpPost);
	}
	
	public static String postObject(String url, Map<String, Object> params)
			throws Exception {

		HttpPost httpPost = new HttpPost(url);
		setEntityObject(httpPost, params);

		return execute(httpPost);
	}

	public static String get(String url, Map<String, String> params)
			throws Exception {

		HttpGet httpGet = new HttpGet(url + getQueryString(params));

		return execute(httpGet);
	}

	private static void setEntity(HttpEntityEnclosingRequestBase request,
			Map<String, String> params) throws Exception {
		List<NameValuePair> postParams = new ArrayList<NameValuePair>();

		if (params != null) {
			if (params.containsKey("")) {
				request.setEntity(new StringEntity(params.get("")));
			} else {
				for (String key : params.keySet()) {
					postParams
							.add(new BasicNameValuePair(key, params.get(key)));
				}

				request.setEntity(new UrlEncodedFormEntity(postParams,
						HTTP.UTF_8));
			}
		}
	}
	private static void setEntityObject(HttpEntityEnclosingRequestBase request,
			Map<String, Object> params) throws Exception {
		List<NameValuePair> postParams = new ArrayList<NameValuePair>();

		if (params != null) {
			if (params.containsKey("")) {
				request.setEntity(new StringEntity((String) params.get("")));
			} else {
				for (String key : params.keySet()) {
					postParams
							.add(new BasicNameValuePair(key, (String) params.get(key)));
				}

				request.setEntity(new UrlEncodedFormEntity(postParams,
						HTTP.UTF_8));
			}
		}
	}

	private static DefaultHttpClient createHttpClient() {
		// Sony Xperia Z, Z1 and ZR phones asks for MMS permissions when using
		// AndroidHttpClient
		// AndroidHttpClient httpClient = AndroidHttpClient.newInstance(null);
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpParams params = httpClient.getParams();
		HttpConnectionParams.setStaleCheckingEnabled(params, true);
		HttpConnectionParams.setConnectionTimeout(params,
				DEFAULT_CONNECT_TIMEOUT_MILLIS);
		HttpConnectionParams.setSoTimeout(params, DEFAULT_READ_TIMEOUT_MILLIS);
		HttpConnectionParams.setSocketBufferSize(params, 8192);
		ConnManagerParams.setTimeout(params,
				DEFAULT_GET_CONNECTION_FROM_POOL_TIMEOUT_MILLIS);

		// 設定自動轉址。 不要自動轉址: false，自動轉址: true
		HttpClientParams.setRedirecting(params, true);

		// Don't handle authentication automatically
		HttpClientParams.setAuthenticating(params, false);

		return httpClient;
	}

	private static String getQueryString(Map<String, String> params) {
		String queryString = "";

		if (params != null) {
			List<NameValuePair> getParams = new ArrayList<NameValuePair>();

			if (params.containsKey("")) {
				queryString = "?" + params.get("");
			} else {
				for (String key : params.keySet()) {
					getParams.add(new BasicNameValuePair(key, params.get(key)));
				}

				queryString = "?" + URLEncodedUtils.format(getParams, "UTF-8");
			}
		}

		return queryString;
	}

}
