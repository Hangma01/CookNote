package com.cooknote.backend.global.infra.mail.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cooknote.backend.global.constants.Constans;
import com.cooknote.backend.global.error.exceptionCode.AuthErrorCode;
import com.cooknote.backend.global.error.excption.CustomAuthException;
import com.cooknote.backend.global.infra.mail.dto.response.VerifyAuthCodeResponseDTO;
import com.cooknote.backend.global.infra.mail.service.MailService;
import com.cooknote.backend.global.message.ErrorMessage;
import com.cooknote.backend.global.message.SuccessMessage;
import com.cooknote.backend.global.utils.CommonFunctionUtil;
import com.cooknote.backend.global.utils.MailContentUtil;
import com.cooknote.backend.global.utils.RedisUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailServiceImpl implements MailService {

    
    private final MailSender mailSender;
    private final RedisUtil redisUtil;
	
    @Override
	public void sendAuthCode(String email) {
    	
    	
		String authCode = CommonFunctionUtil.createMailAuthCode();
		String content = MailContentUtil.createAuthCodeContent(authCode);

		mailSender.sendAuthCode(email, authCode, content);
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
