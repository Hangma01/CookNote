package com.cooknote.backend.domain.category.service;

import org.springframework.stereotype.Service;

import com.cooknote.backend.domain.category.dto.response.CategoryGetAllResponseDTO;
import com.cooknote.backend.domain.category.dto.response.CategoryResponseDTO;

@Service
public interface CategoryService {

	CategoryGetAllResponseDTO getCategoryAll();

	CategoryResponseDTO getCategory();

}
