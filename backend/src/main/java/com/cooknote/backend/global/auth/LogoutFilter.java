//package com.cooknote.backend.global.auth;
//
//import java.io.IOException;
//
//import org.springframework.web.filter.GenericFilterBean;
//
//import com.cooknote.backend.global.constants.Constans;
//import com.cooknote.backend.global.utils.auth.JwtUtil;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.ServletRequest;
//import jakarta.servlet.ServletResponse;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//
//@RequiredArgsConstructor
//public class LogoutFilter extends GenericFilterBean {
//	
//	private final JwtUtil jwtUtil;
//
////	@Override
////	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
////			throws IOException, ServletException {
////		doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
////	}
////	
////	private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
////			throws IOException, ServletException {
////		
////		// 로그아웃이 아닌 URI는 다음 필터로 넘김
////		if (!request.getRequestURI().equals(Constans.LOGOUT_URI)) {
////	        filterChain.doFilter(request, response);
////	        return;
////	    }
////		
////		if(!request.getMethod().equals(Constans.METHOD_POST_TEXT)) {
////			filterChain.doFilter(request, response);
////	        return;
////		}
////		
////		
////		
////		
//		
//	}
//
//}
