package com.cooknote.backend.global.constants;

public class Constans {
    
	// PREFIX
	public static final String PW_RESET_PREFIX = "pwReset";
    public static final String MAIL_AUTH_PREFIX = "authMail";
    public static final String REFRESH_TOKEN_PREFIX = "refreshToken";
    
    
    // TOKEN
    public static final String ACCESS_TOKEN_NAME = "accessToken";
    public static final String REFRESH_TOKEN_NAME = "refreshToken";
    public static final int ACCESS_TOKEN_EXPIRED_MS = 7200000; 		// 60 * 60 * 2 * 1000 => 2시간 
    public static final int REFRESH_TOKEN_EXPIRED_MS = 86400000;	// 60 * 60 * 24 * 1000 => 1일
    
    // JWT
    public static final String CATEGORY_NAME = "category";
    public static final String USER_ID_NAME = "userId";
    
    // URI
    public static final String REISSUE_URI = "/auth/reissue";
    public static final String LOGOUT_URI = "/logout";
    
    
    // Method
    public static final String METHOD_POST_TEXT = "POST";
    
    // ETC...
    public static final int SECOND_MS = 1000;
    public static final int PW_RESET_TOKEN_EXPIRE = 300; 
    
    
    
}
