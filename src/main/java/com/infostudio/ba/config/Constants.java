package com.infostudio.ba.config;

/**
 * Application constants.
 */
public final class Constants {

    // Regex for acceptable logins
    public static final String LOGIN_REGEX = "^[_'.@A-Za-z0-9-]*$";

    public static final String SYSTEM_ACCOUNT = "system";
    public static final String ANONYMOUS_USER = "anonymoususer";
    public static final String DEFAULT_LANGUAGE = "en";
   	public static final int CORRECTIVE_MEASURE_EXPIRED_TIME = 1000 * 3600 * 24; // 24 hours
	public static final String AUTHORIZATION_HEADER = "Authorization";
    private Constants() {
    }
}
