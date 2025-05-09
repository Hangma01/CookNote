package com.cooknote.backend.global.infra.mail.service;

import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.cooknote.backend.global.error.CustomException;
import com.cooknote.backend.global.error.ErrorCode;
import com.cooknote.backend.global.infra.mail.dto.response.VerifyAuthCodeResponseDTO;
import com.cooknote.backend.global.infra.utils.message.ErrorMessageUtil;
import com.cooknote.backend.global.infra.utils.message.SuccessMessageUtil;
import com.cooknote.backend.global.infra.utils.redis.RedisUtil;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

	private final JavaMailSender javaMailSender;
	private final RedisUtil redisUtil;
	private final int authCodeLen = 6;
	private final SecureRandom seucreRandom = new SecureRandom();
	
    @Value("${spring.mail.username}")
    private String from;
    
    @Value("${spring.mail.properties.mail.smtp.auth-code-expire}")
	private long authCodeExpire;
	
    
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
    public void sendAuthCode(String email) {
       
    	String authCode = createAuthCode();												// 인증 번호 생성
    	String content = createAuthCodeContent(authCode);								// 인증 번호용 Content 생성
    	
    	try {
    		MimeMessage message = createMessage(email, "CookNote 이메일인증", content);	// 메시지 셋팅
        	
        	javaMailSender.send(message);												// 메시지 발송	
        	
        	// 레디스에 인증번호 저장
        	redisUtil.setDataExpire(email, authCode, authCodeExpire);
    	} catch (Exception e) {
    		log.error("메일 발송 실패: {}", e.getMessage());
		}
    }
    
    // 인증 번호 검증
    public VerifyAuthCodeResponseDTO verifyAuthCode(String email, String authCode) {

		String redisAuthCode = redisUtil.getData(email);
	
		if(redisAuthCode == null){ 										// 인증 번호 시간 만료
			throw new CustomException(ErrorCode.MAIL_AUTH_CODE_EXPIRE_EXCEPTION);
	    } else if(redisAuthCode.equals(authCode)) {						// 인증 번호 일치
	        return VerifyAuthCodeResponseDTO.builder()
	        		.result(true)
	                .message(SuccessMessageUtil.VERIFY_AUTH_CODE_SUCCESS.getMessage())  
	                .build();
	    } 
	    
		
		return VerifyAuthCodeResponseDTO.builder()							// 인증 번호 불일치
	            .result(false)
				.message(ErrorMessageUtil.VERIFY_AUTH_CODE_NOT_MATCH.getMessage())
	            .build();
    }

	
	// 랜덤 인증 번호 생성
	private String createAuthCode() {
        StringBuilder key = new StringBuilder();

        for (int i = 0; i < authCodeLen; i++) { 	// 인증 번호 6자리
           key.append(seucreRandom.nextInt(10)); 	// 숫자        
        }
        
        return key.toString();
    }
	
    private String createAuthCodeContent(String authCode) {
        return "<h2>인증 코드</h2><p>아래 인증 코드를 입력하세요:</p><strong>" + authCode + "</strong>";
    }
}
