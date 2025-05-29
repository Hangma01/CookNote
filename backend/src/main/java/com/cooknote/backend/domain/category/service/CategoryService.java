package com.cooknote.backend.domain.category.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cooknote.backend.domain.category.dto.response.CategoryGetAllResponseDTO;
import com.cooknote.backend.domain.category.dto.response.CategoryReportReasonResponseDTO;
import com.cooknote.backend.domain.category.dto.response.CategoryResponseDTO;

@Service
public interface CategoryService {

	CategoryGetAllResponseDTO getCategoryAll();

	CategoryResponseDTO getCategory();

	List<CategoryReportReasonResponseDTO> getCategoryReportReason();

}
