package com.cooknote.backend.domain.mail.service.Impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cooknote.backend.domain.mail.dto.response.VerifyAuthCodeResponseDTO;
import com.cooknote.backend.domain.mail.service.MailService;
import com.cooknote.backend.global.constants.Constans;
import com.cooknote.backend.global.error.exceptionCode.AuthErrorCode;
import com.cooknote.backend.global.error.excption.CustomAuthException;
import com.cooknote.backend.global.infra.mail.MailSender;
import com.cooknote.backend.global.message.ErrorMessage;
import com.cooknote.backend.global.message.SuccessMessage;
import com.cooknote.backend.global.utils.content.MailContent;
import com.cooknote.backend.global.utils.random.RandomAuthCodeUtil;
import com.cooknote.backend.global.utils.redis.RedisUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    @Value("${spring.mail.properties.mail.smtp.auth-code-expire}")
	private long authCodeExpire;
    
    private final MailSender mailSender;
    private final RedisUtil redisUtil;
	
    @Override
	public void sendAuthCode(String email) {
		
		String authCode = RandomAuthCodeUtil.createAuthCode();
		String content = MailContent.createAuthCodeContent(authCode);
		String mailAuthRedisKey = Constans.MAIL_AUTH_PREFIX + email;
		
		mailSender.sendAuthCode(email, authCode, content);
		
    	// 레디스에 인증번호 저장
    	redisUtil.setDataExpire(mailAuthRedisKey, authCode, authCodeExpire);
	}
	
    
    // 인증 번호 검증
    @Override
    public VerifyAuthCodeResponseDTO verifyAuthCode(String email, String authCode) {

		String mailAuthRedisKey = Constans.MAIL_AUTH_PREFIX + email;
		String redisAuthCode = redisUtil.getData(mailAuthRedisKey);
		VerifyAuthCodeResponseDTO verifyAuthCodeResponseDTO = null;
	
		if(redisAuthCode == null){ 										// 인증 번호 시간 만료
			throw new CustomAuthException(AuthErrorCode.MAIL_AUTH_CODE_EXPIRED_EXCEPTION);
	    } else if(redisAuthCode.equals(authCode)) {						// 인증 번호 일치		
	    	verifyAuthCodeResponseDTO = VerifyAuthCodeResponseDTO.builder()
													        		.result(true)
													                .message(SuccessMessage.VERIFY_AUTH_CODE_SUCCESS.getMessage())  
													                .build();
	    } else {
	    	verifyAuthCodeResponseDTO= VerifyAuthCodeResponseDTO.builder()							// 인증 번호 불일치
													            .result(false)
																.message(ErrorMessage.VERIFY_AUTH_CODE_NOT_MATCH.getMessage())
													            .build();
	    }
		
		return verifyAuthCodeResponseDTO;
    }

    // 인증번호 삭제
    @Override
	public void deleteAuthCode(String email) {
		String mailAuthRedisKey = Constans.MAIL_AUTH_PREFIX + email;
		redisUtil.deleteData(mailAuthRedisKey);
		
	}
}
