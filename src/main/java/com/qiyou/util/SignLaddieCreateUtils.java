package com.qiyou.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.qiyou.persistence.model.Constants;


@Component
public class SignLaddieCreateUtils {

	private static final Logger logger = LoggerFactory.getLogger(SignLaddieCreateUtils.class);

	protected static final String GET = "GET";
	protected static final String POST = "POST";
	protected static final String PUT = "POST";
	protected static final String DELETE = "DELETE";

	@Value("${account_register.app_id}")
	private String registerAppId;

	@Value("${account_register.app_secret}")
	private String registerAppSecret;

	public String sign(String path, Map<String, String> pathParams, Map<String, String> bodyParams, String method) {
		return sign(path, pathParams, bodyParams, method, null);
	}

	public String sign(String path, Map<String, String> pathParams, Map<String, String> bodyParams, String method, String accessToken) {
		return sign(path, pathParams, bodyParams, method, accessToken, null);
	}

	public String sign(String path, Map<String, String> pathParams, Map<String, String> bodyParams, String method, String accessToken, String originAppId) {
		StringBuilder sign = new StringBuilder();
		sign.append(method).append(path);

		String timestamp = String.valueOf(System.currentTimeMillis());
		Random random = new Random();

		StringBuilder paramString = new StringBuilder();
		pathParams.put(Constants.APP_ID_KEY, registerAppId);
		pathParams.put(Constants.TIMESTAMP_KEY, timestamp);
		if (originAppId != null) {
			bodyParams.put(Constants.ORIGIN_APP_ID_KEY, originAppId);
		}
		if (method.equals(GET) || method.equals(DELETE)) {
			pathParams.put(Constants.NONCE_KEY, String.valueOf(random.nextInt(10000000)));
		} else {
			bodyParams.put(Constants.NONCE_KEY, String.valueOf(random.nextInt(10000000)));
		}


		Map<String, String> params = new HashMap<>();
		params.putAll(pathParams);
		if (method.equals(GET) || method.equals(DELETE)) {
		} else {
			params.putAll(bodyParams);
		}
		List<String> paramsStrList = params.entrySet().stream().filter(d -> !d.getKey().equals("sign")).map(d -> d.getKey() + "=" + d.getValue()).collect(Collectors.toList());
		Collections.sort(paramsStrList);
		for (String parameter : paramsStrList) {
			paramString.append(parameter).append("&");
		}
		if (paramString.length() > 0) {
			sign.append("?").append(paramString.substring(0, paramString.length() - 1)).append(registerAppSecret);
		} else {
			sign.append("?").append(registerAppSecret);
		}
		if (accessToken != null) {
			sign.append(accessToken);
		}
		logger.debug("SigAuthUtil : sign: " + sign.toString());

		return md5(sign.toString());
	}

	public String md5(String input) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(input.getBytes());
			byte[] byteData = md.digest();

			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < byteData.length; ++i) {
				buffer.append(Integer.toString((byteData[i] & 255) + 256, 16).substring(1));
			}
			return buffer.toString();
		} catch (NoSuchAlgorithmException e) {
			return "";
		}
	}

}
