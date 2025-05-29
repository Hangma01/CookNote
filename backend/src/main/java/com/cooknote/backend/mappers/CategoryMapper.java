package com.cooknote.backend.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.cooknote.backend.domain.category.dto.response.CategoryCuisineListResponseDTO;
import com.cooknote.backend.domain.category.dto.response.CategoryPurposeResponseDTO;
import com.cooknote.backend.domain.category.dto.response.CategoryReportReasonResponseDTO;
import com.cooknote.backend.domain.category.entity.CategoryCuisine;
import com.cooknote.backend.domain.category.entity.CategoryPurpose;

@Mapper
public interface CategoryMapper {

	// 카테고리 요리 종류 가져오기
	List<CategoryCuisineListResponseDTO> getCategoryCuisine();
	
	// 카테고리 요리 목적 가져오기
	List<CategoryPurposeResponseDTO> getCategoryPurpose();

	// 카테고리 신고 사유 가져오기
	List<CategoryReportReasonResponseDTO> getCategoryReportReason();

}
