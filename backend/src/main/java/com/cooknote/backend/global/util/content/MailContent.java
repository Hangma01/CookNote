package com.cooknote.backend.global.util.content;

public class MailContent {
    
	public static String createAuthCodeContent(String authCode) {
        return "<h2>인증 코드</h2><p>아래 인증 코드를 입력하세요:</p><strong>" + authCode + "</strong>";
    }
}
