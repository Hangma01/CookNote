package com.cooknote.backend.domain.category.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooknote.backend.domain.category.dto.response.CategoryGetAllResponseDTO;
import com.cooknote.backend.domain.category.service.CategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {

	private final CategoryService categoryService;
	
	// 카테고리 모든 리스트 가져오기
	@GetMapping("/all")
	public ResponseEntity<?> getCategoryAll() {
		
		return ResponseEntity.ok(categoryService.getCategoryAll());
	}

}
