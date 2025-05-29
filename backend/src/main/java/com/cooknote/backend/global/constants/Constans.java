package com.cooknote.backend.global.constants;

public class Constans {
    
	// PREFIX
	public static final String PW_RESET_PREFIX = "pwReset:";
    public static final String MAIL_AUTH_PREFIX = "authMail:";
    public static final String REFRESH_TOKEN_PREFIX = "refreshToken:";
    public static final String BEARER_PREFIX = "Bearer ";
    
    // TOKEN
    public static final String ACCESS_TOKEN_NAME = "accessToken";
    public static final String REFRESH_TOKEN_NAME = "refreshToken";
    public static final int ACCESS_TOKEN_EXPIRED_MS = 1800000; 		// 60 * 30  * 1000 => 30분 1800000
    public static final int REFRESH_TOKEN_EXPIRED_MS = 86400000;	// 60 * 60 * 24 * 1000 => 86400000 1일
    public static final String AUTHORIZATION_HEADER = "Authorization";
    
    // JWT
    public static final String ID_NAME_TEXT = "id";
    public static final String USER_ID_NAME_TEXT = "userId";
    
    // URI
    public static final String REISSUE_URI = "/auth/reissue";
    public static final String LOGIN_URI = "/login";
    public static final String LOGOUT_URI = "/logout";
    
    
    // Method
    public static final String METHOD_POST_TEXT = "POST";
    
    // S3
    public static final String S3_TEMP_IMAGES_PATH = "TempImages/";
    public static final String S3_TEMP_IMAGES_FULL_PATH = "https://cooknote98.s3.ap-northeast-2.amazonaws.com/TempImages/";
    public static final String S3_MOVE_RECIPE_PATH = "Recipe/";
    public static final String S3_MOVE_USER_PROFILE_PATH = "UserProfile/";
    public static final String S3_CONTENT_TYPE = "image/";
    public static final String S3_DEFAULT_START_WITH = "Default";
    
    // GET RECIPE
    public static final int GET_RECIPE_LIMIT = 100;
    public static final int RECOMMENT_RECIPE_MIN_VALUE = 4;
    public static final int SOLO_RECIPE_MIN_VALUE = 12;

    // ETC...
    public static final int SECOND_MS = 1000;
    public static final int PW_RESET_TOKEN_EXPIRE = 3600; 
    public static final String DEFALUT_PROFILE_IMAGE = "https://cooknote98.s3.ap-northeast-2.amazonaws.com/Default/Profile/default_profile.jpg";
    
    
}
