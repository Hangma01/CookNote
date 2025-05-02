package com.cooknote.backend.global.infra.mail.service;

import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailService {

	private final JavaMailSender javaMailSender;	
	private final int authCodeLen = 6;
	private final SecureRandom seucreRandom = new SecureRandom();
	
    @Value("${spring.mail.username}")
    private final String from;
	
    
    // 메시지 셋팅
    private MimeMessage createMessage(String to, String subject, String content) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8"); // true는 멀티파트 메세지를 사용하겠다는 의미이다. (첨부 파일도 가능)
        
        
        helper.setFrom(from);			// bean에 아이디 설정한 것은 단순히 smtp 인증을 받기 위해 사용한 것이라 발신자를 따로 설정해줘야한다.
        helper.setTo(to); 				// 수신자 설정
        helper.setSubject(subject); 	// 메일 제목 설정
        helper.setText(content, true); 	// 메일 내용 설정이고, true는 html을 사용하겠다는 의미이다.
        
        return message;
    }
    
    
    
    // 인증 코드 메일 발송
    public MimeMessage sendAuthCode(String to) throws MessagingException {
       
    	String authCode = createAuthCode();								// 인증 코드 생성
    	String content = createAuthCodeContent(authCode);				// 인증 코드용 Content 생성
    	MimeMessage message = createMessage(to, "이메일인증", content);	// 메시지 셋팅
    	
    	javaMailSender.send(message);									// 메시지 발송
    	
        return message;
    }
	


	
	// 랜덤 인증 코드 생성
	private String createAuthCode() {
        StringBuilder key = new StringBuilder();

        for (int i = 0; i < authCodeLen; i++) { // 인증 코드 6자리
           key.append(seucreRandom.nextInt(10)); // 숫자        
        }
        
        return key.toString();
    }
	
    private String createAuthCodeContent(String authCode) {
        return "<h2>인증 코드</h2><p>아래 인증 코드를 입력하세요:</p><strong>" + authCode + "</strong>";
    }

}
