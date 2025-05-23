package com.cooknote.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode;

@SpringBootApplication
// PageImpl 사용시 객체가 가지고 있는 내부 구조를 그대로 직렬화하면 문제가 생기므로
// DTO를 사용해 직렬화 하여 간단한 데이터 전송 객체로 변환해서 전송한다.

public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

}
