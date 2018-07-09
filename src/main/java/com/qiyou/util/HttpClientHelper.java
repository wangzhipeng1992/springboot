package com.qiyou.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class HttpClientHelper {

	private static final String APPLICATION_X_WWW_FORM_URLENCODED = ContentType.APPLICATION_FORM_URLENCODED
			.getMimeType();
	private static final String APPLICATION_JSON = ContentType.APPLICATION_JSON.getMimeType();

	private static final String CHARTSET = "UTF-8";

	private static final int CONNTIMEOUT = 60000;

	private static final int READTIMEOUT = 60000;

	private static final int MAX_RETRY = 3;

	private static CloseableHttpClient httpClient = null;

	static {
		ConnectionSocketFactory plainsf = PlainConnectionSocketFactory.getSocketFactory();

		LayeredConnectionSocketFactory sslsf = SSLConnectionSocketFactory.getSocketFactory();

		Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory> create()
				.register("http", plainsf).register("https", sslsf).build();

		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);

		// 将最大连接数增加到1000
		cm.setMaxTotal(50);

		// 将每个路由基础的连接增加到100
		cm.setDefaultMaxPerRoute(10);

		httpClient = HttpClients.custom().setConnectionManager(cm).build();
	}

	public static <T> T postForObject(String url, String body, Class<T> toClass) throws IOException {
		String res = post(url, body, APPLICATION_X_WWW_FORM_URLENCODED, CHARTSET, CONNTIMEOUT, READTIMEOUT);
		return JsonHelper.parseToObject(res, toClass);
	}

	public static Map<?, ?> postForMap(String url, String body) throws IOException {
		String res = post(url, body, APPLICATION_X_WWW_FORM_URLENCODED, CHARTSET, CONNTIMEOUT, READTIMEOUT);
		return JsonHelper.parseToMap(res);
	}

	public static String post(String url, String body) throws IOException {
		return post(url, body, APPLICATION_X_WWW_FORM_URLENCODED, CHARTSET, CONNTIMEOUT, READTIMEOUT);
	}

	public static String post(String url, String body, String charset, Integer connTimeout, Integer readTimeout)
			throws IOException {
		return post(url, body, APPLICATION_X_WWW_FORM_URLENCODED, charset, connTimeout, readTimeout);
	}

	public static String postJsonData(String url, String body) throws IOException {
		return post(url, body, APPLICATION_JSON, CHARTSET, CONNTIMEOUT, READTIMEOUT);
	}

	public static <T> T postJsonData(String url, String body, Class<T> toClass) throws IOException {
		String res = post(url, body, APPLICATION_JSON, CHARTSET, CONNTIMEOUT, READTIMEOUT);
		return JsonHelper.parseToObject(res, toClass);
	}

	public static <T> T postFormData(String url, Map<String, String> params, Class<T> toClass) throws IOException {
		String res = postForm(url, params, null, CONNTIMEOUT, READTIMEOUT);
		return JsonHelper.parseToObject(res, toClass);
	}

	public static String postFormData(String url, Map<String, String> params) throws IOException {
		return postForm(url, params, null, CONNTIMEOUT, READTIMEOUT);
	}
	
	public static String postFormData2(String url, Map<String, Object> params) throws IOException {
		return postForm2(url, params, null, CONNTIMEOUT, READTIMEOUT);
	}

	public static String postFormData(String url, Map<String, String> params, Integer connTimeout, Integer readTimeout)
			throws IOException {
		return postForm(url, params, null, connTimeout, readTimeout);
	}

	public static <T> T getForObject(String url, Class<T> toClass) throws UnsupportedOperationException, IOException {
		String res = get(url, CHARTSET, CONNTIMEOUT, READTIMEOUT);
		return JsonHelper.parseToObject(res, toClass);
	}

	public static String get(String url) throws IOException {
		return get(url, CHARTSET, CONNTIMEOUT, READTIMEOUT);
	}

	public static String get(String url, Map<String, String> map) throws IOException {
		StringBuffer sb = new StringBuffer(url);
		if (MapUtils.isNotEmpty(map)) {
			sb.append("?");
			for (Map.Entry<String, String> set : map.entrySet()) {
				sb.append(set.getKey()).append("=").append(set.getValue()).append("&");
			}
		}
		return get(sb.toString());
	}

	public static String getbyObj(String url, Map<String, Object> map) throws IOException {
		StringBuffer sb = new StringBuffer(url);
		if (MapUtils.isNotEmpty(map)) {
			sb.append("?");
			for (Map.Entry<String, Object> set : map.entrySet()) {
				sb.append(set.getKey()).append("=").append(set.getValue()).append("&");
			}
		}
		String substring = sb.substring(sb.length() - 1, sb.length());
		if (substring.equals("&")) {
			sb.deleteCharAt(sb.length() - 1);
		}
		return get(sb.toString());
	}

	public static String get(String url, String charset) throws IOException {
		return get(url, charset, CONNTIMEOUT, READTIMEOUT);
	}

	public static String post(String url, String body, String mimeType, String charset, Integer connTimeout,
			Integer readTimeout) throws IOException {
		HttpPost post = new HttpPost(url);
		try {
			if (StringUtils.isNotBlank(body)) {
				HttpEntity entity = new StringEntity(body, ContentType.create(mimeType, charset));
				post.setEntity(entity);
			}
			RequestConfig customReqConfig = getCustomReqConfig(connTimeout, readTimeout);
			post.setConfig(customReqConfig);
			HttpResponse res = httpClient.execute(post);
			return EntityUtils.toString(res.getEntity(), charset);
		} finally {
			post.releaseConnection();
		}
	}

	public static String postForm(String url, Map<String, String> params, Map<String, String> headers,
			Integer connTimeout, Integer readTimeout) throws IOException {
		HttpPost post = new HttpPost(url);
		try {
			if (params != null && !params.isEmpty()) {
				List<NameValuePair> formParams = new ArrayList<>();
				Set<Entry<String, String>> entrySet = params.entrySet();
				for (Entry<String, String> entry : entrySet) {
					formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
				}
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formParams, Consts.UTF_8);
				post.setEntity(entity);
			}
			if (headers != null && !headers.isEmpty()) {
				for (Entry<String, String> entry : headers.entrySet()) {
					post.addHeader(entry.getKey(), entry.getValue());
				}
			}
			RequestConfig customReqConfig = getCustomReqConfig(connTimeout, readTimeout);
			post.setConfig(customReqConfig);
			HttpResponse res = httpClient.execute(post);
			return EntityUtils.toString(res.getEntity(), CHARTSET);
		} finally {
			post.releaseConnection();
		}
	}

	public static String postForm2(String url, Map<String, Object> params, Map<String, String> headers,
			Integer connTimeout, Integer readTimeout) throws IOException {
		HttpPost post = new HttpPost(url);
		try {
			if (params != null && !params.isEmpty()) {
				List<NameValuePair> formParams = new ArrayList<>();
				Set<Entry<String, Object>> entrySet = params.entrySet();
				for (Entry<String, Object> entry : entrySet) {
					formParams.add(new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue())));
				}
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formParams, Consts.UTF_8);
				post.setEntity(entity);
			}
			if (headers != null && !headers.isEmpty()) {
				for (Entry<String, String> entry : headers.entrySet()) {
					post.addHeader(entry.getKey(), entry.getValue());
				}
			}
			RequestConfig customReqConfig = getCustomReqConfig(connTimeout, readTimeout);
			post.setConfig(customReqConfig);
			HttpResponse res = httpClient.execute(post);
			return EntityUtils.toString(res.getEntity(), CHARTSET);
		} finally {
			post.releaseConnection();
		}
	}
	
	public static String get(String url, String charset, Integer connTimeout, Integer readTimeout)
			throws UnsupportedOperationException, IOException {
		HttpGet get = new HttpGet(url);
		try {
			RequestConfig customReqConfig = getCustomReqConfig(connTimeout, readTimeout);
			get.setConfig(customReqConfig);
			HttpResponse res = httpClient.execute(get);
			return EntityUtils.toString(res.getEntity(), charset);
		} finally {
			get.releaseConnection();
		}
	}

	private static RequestConfig getCustomReqConfig(Integer connTimeout, Integer readTimeout) {
		Builder customReqConf = RequestConfig.custom();
		if (connTimeout != null) {
			customReqConf.setConnectTimeout(connTimeout);
		}
		if (readTimeout != null) {
			customReqConf.setSocketTimeout(readTimeout);
		}
		return customReqConf.build();
	}

	// 审批
	public static String post(String remoteUrl, Map<String, String> params) throws IOException {
		HttpPost post = new HttpPost(remoteUrl);
		try {
			if (params != null && !params.isEmpty()) {
				List<NameValuePair> formParams = new ArrayList<>();
				Set<Entry<String, String>> entrySet = params.entrySet();
				for (Entry<String, String> entry : entrySet) {
					formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
				}
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formParams, Consts.UTF_8);
				post.setEntity(entity);
			}
			HttpResponse res = httpClient.execute(post);
			return EntityUtils.toString(res.getEntity(), CHARTSET);
		} finally {
			post.releaseConnection();
		}
	}

	public static String put(String url, Map<Object, String> map)
			throws UnsupportedOperationException, IOException, URISyntaxException {
		Gson gson = new GsonBuilder().create();
		String bodyParams = gson.toJson(map);
		URIBuilder builder = new URIBuilder(url);
		HttpPut httpPut = new HttpPut(builder.build());
		httpPut.setHeader("Content-type", "application/json");
		try {
			httpPut.setEntity(new StringEntity(bodyParams, "UTF-8"));
			HttpResponse res = httpClient.execute(httpPut);
			return EntityUtils.toString(res.getEntity(), CHARTSET);
		} finally {
			httpPut.releaseConnection();
		}
	}

	/**
	 * Laddie添加Sign访问-获取指定小哥详情接口:GET方式
	 * 
	 * @param url
	 * @param map
	 * @return
	 * @throws IOException
	 */
	public static String getAppointWithSign(SignLaddieCreateUtils signLaddieCreateUtils, String signUrl, String url,
			Map<String, String> map) throws IOException {
		String sign = signLaddieCreateUtils.sign(signUrl, map, null, "GET");
		map.put("sign", sign);
		StringBuffer sb = new StringBuffer(url);
		if (MapUtils.isNotEmpty(map)) {
			sb.append("?");
			for (Map.Entry<String, String> set : map.entrySet()) {
				sb.append(set.getKey()).append("=").append(set.getValue()).append("&");
			}
		}
		String substring = sb.substring(sb.length() - 1, sb.length());
		if (substring.equals("&")) {
			sb.deleteCharAt(sb.length() - 1);
		}
		return get(sb.toString());
	}

	/**
	 * 小哥可用时间查询-获取时间段内所有小哥信息:GET方式
	 * 
	 * @param url
	 * @param map
	 * @return
	 * @throws IOException
	 */
	public static String getAllWithSign(SignLaddieCreateUtils signLaddieCreateUtils, String signUrl, String url,
			Map<String, String> map) throws IOException {
		String sign = signLaddieCreateUtils.sign(signUrl, map, null, "GET");
		map.put("sign", sign);
		StringBuffer sb = new StringBuffer(url);
		if (MapUtils.isNotEmpty(map)) {
			sb.append("?");
			for (Map.Entry<String, String> set : map.entrySet()) {
				sb.append(set.getKey()).append("=").append(set.getValue()).append("&");
			}
		}
		String substring = sb.substring(sb.length() - 1, sb.length());
		if (substring.equals("&")) {
			sb.deleteCharAt(sb.length() - 1);
		}
		return get(sb.toString());
	}

	/**
	 * Laddie添加Sign访问-占用小哥接口:POST方式
	 * 
	 * @param remoteUrl
	 * @param params
	 * @return
	 * @throws IOException
	 */
	public static String postOccupyWithSign(SignLaddieCreateUtils signLaddieCreateUtils, String signUrl,
			String remoteUrl, Map<String, String> params) throws IOException {
		Map<String, String> signParamMap = new HashMap<String, String>();
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, String> set : params.entrySet()) {
			sb.append(set.getKey()).append("=").append(set.getValue()).append("&");
		}
		signParamMap.put("jsonBody", sb.substring(0, sb.length() - 1).toString());
		String sign = signLaddieCreateUtils.sign(signUrl, params, signParamMap, "POST");
		params.put("sign", sign);
		HttpPost post = new HttpPost(remoteUrl);
		try {
			if (params != null && !params.isEmpty()) {
				List<NameValuePair> formParams = new ArrayList<>();
				Set<Entry<String, String>> entrySet = params.entrySet();
				for (Entry<String, String> entry : entrySet) {
					formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
				}
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formParams, Consts.UTF_8);
				post.setEntity(entity);
			}
			HttpResponse res = httpClient.execute(post);
			return EntityUtils.toString(res.getEntity(), CHARTSET);
		} finally {
			post.releaseConnection();
		}
	}

	/**
	 * SSO跳过证书验证
	 */
	public static HttpClient wrapClient(HttpClient base) {
		try {
			// TODO 考虑优化,是否需要反复执行
			SSLContext ctx = SSLContext.getInstance("TLS");
			X509TrustManager tm = new X509TrustManager() {

				public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
						throws java.security.cert.CertificateException {
				}

				public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
						throws java.security.cert.CertificateException {
				}

				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};
			ctx.init(null, new TrustManager[] { tm }, null);
			SSLSocketFactory ssf = new SSLSocketFactory(ctx);
			ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			ClientConnectionManager ccm = base.getConnectionManager();
			SchemeRegistry sr = ccm.getSchemeRegistry();
			sr.register(new Scheme("https", ssf, 443));
			return new DefaultHttpClient(ccm, base.getParams());
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static Object sendRequest(String url, Map map) {
		boolean requireHttps = false;
		if (url.startsWith("https") || url.startsWith("HTTPS")) {
			requireHttps = true;
		}
		HttpClient httpclient = new DefaultHttpClient();
		HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
		if (requireHttps) {
			httpclient = wrapClient(httpclient);
			SchemeRegistry registry = new SchemeRegistry();
			SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
			socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
			registry.register(new Scheme("https", socketFactory, 443));
			SingleClientConnManager mgr = new SingleClientConnManager(httpclient.getParams(), registry);
			DefaultHttpClient httpClient = new DefaultHttpClient(mgr, httpclient.getParams());
			HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
		}
		HttpPost httpPost = new HttpPost(url);
		List<NameValuePair> qparams = new ArrayList<NameValuePair>();
		Iterator iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String key = (String) entry.getKey();
			String val = entry.getValue().toString();
			qparams.add(new BasicNameValuePair(key, val));
		}
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(qparams, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e1) {
			// HttpLogUtil.exceptionLog(e1);
		}
		String responseStr = null;
		try {
			ResponseHandler<byte[]> handler = new ResponseHandler<byte[]>() {
				public byte[] handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
					HttpEntity entity = response.getEntity();
					if (entity != null) {
						return EntityUtils.toByteArray(entity);
					} else {
						return null;
					}
				}
			};
			byte[] response = httpclient.execute(httpPost, handler);
			responseStr = new String(response, "UTF-8");
			return responseStr;
		} catch (Exception e) {
			System.out.println("错误的responseStr" + responseStr);
			// HttpLogUtil.exceptionLog(e);
		}
		return null;
	}

}
