package com.cooknote.backend.global.constants;

public class Constans {
    
	// PREFIX
	public static final String PW_RESET_PREFIX = "pwReset:";
    public static final String MAIL_AUTH_PREFIX = "authMail:";
    public static final String REFRESH_TOKEN_PREFIX = "refreshToken:";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String BLACKLIST_PREFIX = "blacklist:";
    
    // TOKEN
    public static final String ACCESS_TOKEN_NAME = "accessToken";
    public static final String REFRESH_TOKEN_NAME = "refreshToken";
    public static final int ACCESS_TOKEN_EXPIRED_MS = 1800000; 		// 60 * 30  * 1000 => 30분 1800000
    public static final int REFRESH_TOKEN_EXPIRED_MS = 86400000;	// 60 * 60 * 24 * 1000 => 86400000 1일
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final int BLACKLIST_EXPIRED_SEC = 3600;			// 60 * 60 => 1시간 3600
    
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
    public static final String S3_MINI_LOGO_IMAGE_PATH = "https://cooknote98.s3.ap-northeast-2.amazonaws.com/Logo/Logo_icon.png";
    public static final String S3_MOVE_RECIPE_PATH = "Recipe/";
    public static final String S3_MOVE_USER_PROFILE_PATH = "UserProfile/";
    public static final String S3_CONTENT_TYPE = "image/";
    public static final String S3_DEFAULT_START_WITH = "Default";
    
    // GET RECIPE
    public static final int GET_RECIPE_LIMIT = 30;
    public static final int RECOMMENT_RECIPE_MIN_VALUE = 4;
    public static final int SOLO_RECIPE_MIN_VALUE = 4;
    public static final int BEST_RECIPE_MIN_VALUE = 12;
    public static final int RECENT_RECIPE_MIN_VALUE = 12;

    // WEIGHT
    public static final double CHEF_FOLLOW_WEIGHT = 0.4;
    public static final double CHEF_RECIPE_WEIGHT = 0.2;
    public static final double CHEF_LIKE_WEIGHT = 0.2;
    public static final double CHEF_BOOKMARK_WEIGHT = 0.2;
    
    public static final double RECIPE_LIKE_WEIGHT = 0.7;
    public static final double RECIPE_COMMENT_WEIGHT = 0.3;
    
    // REPORT
    public static final String REPORT_TYPE_COMMENT_TEXT = "댓글";
    public static final String REPORT_TYPE_REPLY_TEXT = "답글";
    public static final String REPORT_TYPE_RECIPE_TEXT = "레시피";
    
    // ETC...
    public static final int SECOND_MS = 1000;
    public static final int PW_RESET_TOKEN_EXPIRE = 3600; 
    public static final String DEFALUT_PROFILE_IMAGE = "https://cooknote98.s3.ap-northeast-2.amazonaws.com/Default/Profile/default_profile.jpg";
    
    
}
