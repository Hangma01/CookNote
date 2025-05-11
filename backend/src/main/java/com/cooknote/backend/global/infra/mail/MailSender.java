package com.cooknote.backend.global.infra.mail;

import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.cooknote.backend.domain.mail.dto.response.VerifyAuthCodeResponseDTO;
import com.cooknote.backend.global.error.CustomException;
import com.cooknote.backend.global.error.ErrorCode;
import com.cooknote.backend.global.util.message.ErrorMessageUtil;
import com.cooknote.backend.global.util.message.SuccessMessageUtil;
import com.cooknote.backend.global.util.redis.RedisUtil;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailSender {

	private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;
	
    
    // 메시지 셋팅
    private MimeMessage createMessage(String email, String subject, String content) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8"); // true는 멀티파트 메세지를 사용하겠다는 의미이다. (첨부 파일도 가능)
        
        
        helper.setFrom(from);			// bean에 아이디 설정한 것은 단순히 smtp 인증을 받기 위해 사용한 것이라 발신자를 따로 설정해줘야한다.
        helper.setTo(email); 			// 수신자 설정
        helper.setSubject(subject); 	// 메일 제목 설정
        helper.setText(content, true); 	// 메일 내용 설정이고, true는 html을 사용하겠다는 의미이다.
        
        return message;
    }
    
    
    // 인증 번호 메일 발송
    @Async
    public void sendAuthCode(String email, String authCode, String content) {
       
    	try {
    		MimeMessage message = createMessage(email, "CookNote 이메일인증", content);	// 메시지 셋팅
        	
        	javaMailSender.send(message);												// 메시지 발송	
    	} catch (MessagingException e) {
    		log.error("메일 발송 실패: {}", e.getMessage());
		}
    }

}
