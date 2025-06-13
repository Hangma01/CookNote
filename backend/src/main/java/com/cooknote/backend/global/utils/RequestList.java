package com.cooknote.backend.global.utils;



import org.springframework.data.domain.Pageable;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class RequestList<T> {
	
	private T data;
	private Pageable pageable;
}
