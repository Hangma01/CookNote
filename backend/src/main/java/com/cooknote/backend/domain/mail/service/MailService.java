package com.cooknote.backend.domain.mail.service;

import org.eclipse.angus.mail.util.MailSSLSocketFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cooknote.backend.domain.mail.dto.response.VerifyAuthCodeResponseDTO;
import com.cooknote.backend.global.error.CustomException;
import com.cooknote.backend.global.error.ErrorCode;
import com.cooknote.backend.global.infra.mail.MailSender;
import com.cooknote.backend.global.util.content.MailContent;
import com.cooknote.backend.global.util.message.ErrorMessageUtil;
import com.cooknote.backend.global.util.message.SuccessMessageUtil;
import com.cooknote.backend.global.util.random.RandomAuthCodeUtil;
import com.cooknote.backend.global.util.redis.RedisUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailService {

    @Value("${spring.mail.properties.mail.smtp.auth-code-expire}")
	private long authCodeExpire;
    
    private final MailSender mailSender;
    private final RedisUtil redisUtil;
	
	public void sendAuthCode(String email) {
		
		String authCode = RandomAuthCodeUtil.createAuthCode();
		String content = MailContent.createAuthCodeContent(authCode);
		
		mailSender.sendAuthCode(email, authCode, content);
		
    	// 레디스에 인증번호 저장
    	redisUtil.setDataExpire(email, authCode, authCodeExpire);
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
}
