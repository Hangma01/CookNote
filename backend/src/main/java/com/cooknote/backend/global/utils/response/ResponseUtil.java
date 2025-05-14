package com.cooknote.backend.global.utils.response;

import java.io.IOException;

import jakarta.servlet.http.HttpServletResponse;

public class ResponseUtil {
    public static void writeJson(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"message\": \"" + message + "\"}");
    }

	public static void writeJson(HttpServletResponse response, int status) {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");
	}
}
