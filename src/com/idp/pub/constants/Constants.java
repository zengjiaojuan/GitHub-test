package com.idp.pub.constants;

import java.util.HashMap;
import java.util.Map;

import com.easemob.server.comm.PropertiesUtils;

public abstract class Constants {

	public static final String SUCCESS = "success";

	public static final String TRUE = "true";

	public static final String FALSE = "false";

	public static final String MESSAGE = "message";

	public static final String ERROR_CODE = "errorCode";

	public static final String USER_INFO = "userInfo";

	public static final String USER_INFO_LISTENER = "usersmonitorlistener";

	public static final String CONTEXT_PATH = "contextPath";
	public static String APPKEY = PropertiesUtils.getProperties().getProperty("APPKEY");

	public static Map<String, Object> MAP() {
		return new HashMap<String, Object>();
	}
}
