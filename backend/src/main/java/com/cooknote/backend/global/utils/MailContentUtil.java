package com.cooknote.backend.global.utils;

import com.cooknote.backend.global.constants.Constans;

public class MailContentUtil {
    
	public static String createAuthCodeContent(String authCode) {
	
        return  "<div style=\"max-width:600px;margin:0 auto;padding:40px;border:1px solid #ddd;" +
		        "font-family:'맑은 고딕',sans-serif;background:#fff\">" +
		        	"<div style=\"display:flex;align-items:center;gap:8px;color:#C09370;font-size:20px;font-weight:bold;\">" +
		        		"<img src='" + Constans.S3_MINI_LOGO_IMAGE_PATH + "' " +
		        		"alt='NAVER' style='width:22px;height:22px;margin-right:8px;padding-top:4px;vertical-align:middle;'/>" +
		        		"CookNote" +
		        	"</div>" +
		        	"<h2 style=\"margin-top:40px;font-size:22px;\">요청하신 인증번호를 <span style=\"color:#2DB400;\">발송해드립니다.</span></h2>" +
		        	"<p style=\"font-size:14px;margin-top:20px;\">아래의 인증번호를 인증번호 입력창에 입력해 주세요. 유효시간은 3분입니다.</p>" +
		        	"<p style=\"font-size:20px;color:#2DB400;font-weight:bold;margin:20px 0;\">인증번호 : " + authCode + "</p>" +
		        	"<p style=\"font-size:14px;line-height:1.6;\">CookNote를 이용해 주셔서 감사합니다.<br>" +
		        	"더욱 편리한 서비스를 제공하기 위해 항상 최선을 다하겠습니다.</p>" +
		        	"<div style=\"margin-top:30px;font-size:12px;color:#888;border-top:1px solid #eee;padding-top:10px;\">" +
		        		"Copyright © CookNote Corp. All Rights Reserved." +
		        	"</div>" +
		        "</div>";
    }
}
