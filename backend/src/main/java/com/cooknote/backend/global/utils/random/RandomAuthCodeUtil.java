package com.cooknote.backend.global.utils.random;

import java.security.SecureRandom;

public class RandomAuthCodeUtil {

    private static final int AUTH_CODE_LEN = 6;
    private static final SecureRandom secureRandom = new SecureRandom();

    public static String createAuthCode() {
        StringBuilder key = new StringBuilder();

        for (int i = 0; i < AUTH_CODE_LEN; i++) {
            key.append(secureRandom.nextInt(10));
        }

        return key.toString();
    }
}