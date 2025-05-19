package com.cooknote.backend.domain.category.service.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cooknote.backend.domain.category.dto.response.CategoryCuisineListResponseDTO;
import com.cooknote.backend.domain.category.dto.response.CategoryGetAllResponseDTO;
import com.cooknote.backend.domain.category.dto.response.CategoryPurposeResponseDTO;
import com.cooknote.backend.domain.category.service.CategoryService;
import com.cooknote.backend.domain.recipe.dto.response.RecipeEnumLabelResponseDTO;
import com.cooknote.backend.domain.recipe.enums.RecipeDuration;
import com.cooknote.backend.domain.recipe.enums.RecipeLevel;
import com.cooknote.backend.domain.recipe.enums.RecipeServing;
import com.cooknote.backend.global.error.exceptionCode.CommonErrorCode;
import com.cooknote.backend.global.error.excption.CustomCommonException;
import com.cooknote.backend.mappers.CategoryMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

	private final CategoryMapper categoryMapper;
	
	// 카테고리 모든 리스트 가져오기
	@Override
	public CategoryGetAllResponseDTO getCategoryAll() {
		// 카테고리 요리 종류 리스트 가져오기
		List<CategoryCuisineListResponseDTO> rspCategoryCuisineList = categoryMapper.getCategoryCuisine();
		
		if(rspCategoryCuisineList == null) {
				throw new CustomCommonException(CommonErrorCode.NOT_FOUND_EXCEPTION);
			}
		
		// 카테고리 요리 목적 리스트 가져오기
		List<CategoryPurposeResponseDTO> rspCategoryPurposeList = categoryMapper.getCategoryPurpose();
	
		if(rspCategoryPurposeList == null) {
			throw new CustomCommonException(CommonErrorCode.NOT_FOUND_EXCEPTION);
		}
		
		// 레시피 인원수 리스트 가져오기
		List<RecipeEnumLabelResponseDTO> recipeServingList = Arrays.stream(RecipeServing.values())
																	.map(v -> new RecipeEnumLabelResponseDTO(v.name(), v.getLabel()))
																	.toList();
		// 레시피 요리시간 리스트 가져오기
		List<RecipeEnumLabelResponseDTO> recipeDurationList = Arrays.stream(RecipeDuration.values())
																	.map(v -> new RecipeEnumLabelResponseDTO(v.name(), v.getLabel()))
																	.toList();
		// 레시피 난이도 리스트 가져오기
		List<RecipeEnumLabelResponseDTO> recipeLevelList = Arrays.stream(RecipeLevel.values())
															 	  .map(v -> new RecipeEnumLabelResponseDTO(v.name(), v.getLabel()))
																  .toList();
		// 위 5개의 리스트를 한 곳에 담아 응답하기
		CategoryGetAllResponseDTO categoryGetAllResponse = CategoryGetAllResponseDTO.builder()
																			  .categoryCuisineList(rspCategoryCuisineList)
																			  .categoryPurposeList(rspCategoryPurposeList)
																			  .recipeServingList(recipeServingList)
																			  .recipeDurationList(recipeDurationList)
																			  .recipeLevelList(recipeLevelList)
																			  .build();
		
		return categoryGetAllResponse;
    }
	

}
