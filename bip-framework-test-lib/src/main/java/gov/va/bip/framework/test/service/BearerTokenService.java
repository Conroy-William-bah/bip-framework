package gov.va.bip.framework.test.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.entity.ContentType;

import gov.va.bip.framework.test.util.RESTUtil;

/**
 * It Fetches token from the token API. The token will be used as a header while
 * invoking actual end points.
 * 
 * @author sravi
 *
 */

public class BearerTokenService {

	private static BearerTokenService instance = null;
	private String bearerToken = "";

	private BearerTokenService() {

	}

	public static BearerTokenService getInstance() {

		if (instance == null) {
			instance = new BearerTokenService();
			instance.bearerToken = getToken("token.Request");
		}
		return instance;
	}

	public static String getTokenByHeaderFile(final String headerFile) {
		return getToken(headerFile);
	}

	/**
	 * Makes API call to bearer token service and returns the token as string.
	 * 
	 * @param headerFile
	 * @return
	 */
	public static String getToken(final String headerFile) {
		final RESTConfigService restConfig = RESTConfigService.getInstance();
		final String baseUrl = restConfig.getProperty("baseURL", true);
		final String tokenUrl = restConfig.getProperty("tokenUrl");
		final Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Accept", ContentType.APPLICATION_JSON.getMimeType());
		headerMap.put("Content-Type", ContentType.APPLICATION_JSON.getMimeType());
		RESTUtil restUtility = new RESTUtil();
		restUtility.setUpRequest(headerFile, headerMap);
		return restUtility.postResponse(baseUrl + tokenUrl);
	}

	public String getBearerToken() {
		return bearerToken;
	}

}
