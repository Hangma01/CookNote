package com.cooknote.backend.domain.recipe.service.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cooknote.backend.domain.recipe.dto.request.RecipeSeqRequestDTO;
import com.cooknote.backend.domain.recipe.dto.response.RecipeEditResponseDTO;
import com.cooknote.backend.domain.recipe.dto.response.RecipeIngredientResponseDTO;
import com.cooknote.backend.domain.recipe.dto.response.RecipeSeqResponseDTO;
import com.cooknote.backend.domain.category.dto.response.CategoryGetAllResponseDTO;
import com.cooknote.backend.domain.category.service.CategoryService;
import com.cooknote.backend.domain.recipe.dto.request.RecipeSaveRequestDTO;
import com.cooknote.backend.domain.recipe.entity.Recipe;
import com.cooknote.backend.domain.recipe.service.RecipeService;
import com.cooknote.backend.global.error.exceptionCode.CommonErrorCode;
import com.cooknote.backend.global.error.excption.CustomCommonException;
import com.cooknote.backend.global.infra.aws.s3.service.S3Service;
import com.cooknote.backend.mappers.RecipeIngredientMapper;
import com.cooknote.backend.mappers.RecipeMapper;
import com.cooknote.backend.mappers.RecipeSeqMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecipeServiceImpl implements RecipeService {

	private final RecipeMapper recipeMapper;
	private final RecipeIngredientMapper recipeIngredientMapper;
	private final RecipeSeqMapper recipeSeqMapper;
	private final S3Service s3Service;
	private final CategoryService categoryService;

	// 레시피 저장
	@Override
	@Transactional
	public void recipeSave(long userId, RecipeSaveRequestDTO recipeSaveRequestDTO) {
		
		
		
		// S3 이미지 temp -> recipe로 이동
		String thumbnail = null;
        List<String> s3ConvertSeqList = new ArrayList<>();
        Map<String, Object> s3ConvertReq = new HashMap<>();
        Map<String,Object> getS3Urls = null;
	
		thumbnail = recipeSaveRequestDTO.getThumbnail();

		s3ConvertReq.put("thumbnail", thumbnail);
		
        for (RecipeSeqRequestDTO recipeSeqRequestDTO : recipeSaveRequestDTO.getRecipeSeqList()) {
        	s3ConvertSeqList.add(recipeSeqRequestDTO.getImage());
        }
        
        s3ConvertReq.put("images", s3ConvertSeqList);

        getS3Urls = s3Service.convertedGetUrls(s3ConvertReq);
		
        
        try {
        	// 레시피 저장
	        Recipe reqRecipe = Recipe.builder()
					.title(recipeSaveRequestDTO.getTitle())
					.description(recipeSaveRequestDTO.getDescription())
					.thumbnail((String)getS3Urls.get("thumbnail"))
					.videoId(recipeSaveRequestDTO.getVideoId())
					.serving(recipeSaveRequestDTO.getServing())
					.duration(recipeSaveRequestDTO.getDuration())
					.level(recipeSaveRequestDTO.getLevel())
					.tip(recipeSaveRequestDTO.getTip())
					.status(recipeSaveRequestDTO.getStatus())
					.categoryCuisineId(recipeSaveRequestDTO.getCategoryCuisineId())
					.categoryPurposeId(recipeSaveRequestDTO.getCategoryPurposeId())
					.writerId(userId)
					.build();
		    recipeMapper.recipeSave(reqRecipe);
			
		    // 재료 저장
		    recipeIngredientMapper.recipeIngredientSave(recipeSaveRequestDTO.getRecipeIngredientList(), reqRecipe.getRecipeId());
		    
        	// 레시피 순서 저장
		    List<RecipeSeqRequestDTO> recipeSeqList = recipeSaveRequestDTO.getRecipeSeqList();
		    List<String> recipeSeqImageUrls = (List<String>) getS3Urls.get("images");
		
			for (int i = 0; i < recipeSeqList.size(); i++) {
				recipeSeqList.get(i).setImage(recipeSeqImageUrls.get(i));
			}
		    recipeSeqMapper.recipeSeqSave(recipeSaveRequestDTO.getRecipeSeqList(), reqRecipe.getRecipeId());
		    
		    
		} catch (Exception e) {
	        try {
		        s3Service.deleteImagesFromS3(getS3Urls);				
			} catch (Exception error) {
				log.error("s3 이미지 삭제 실패: " + thumbnail + ", " + String.join(", ", s3ConvertSeqList));
			}
	        
	       throw new CustomCommonException(CommonErrorCode.UPDATE_EXCEPTION);
		}
	}

	// 레시피 데이터 가져오기 - 수정
	@Override
	public RecipeEditResponseDTO getRecipeForEdit(long userId, String recipeId) {
		
		// 레시피 데이터 가져오기
		RecipeEditResponseDTO recipeEditResponseDTO = recipeMapper.getRecipeForEdit(userId, recipeId);
		
		checkNull(recipeEditResponseDTO);
		
		// 재료 데이터 가져오기
		List<RecipeIngredientResponseDTO> recipeIngredientResponseDTO = recipeMapper.getRecipeIngredient(recipeId);
		
		checkNull(recipeIngredientResponseDTO);
		
		
		// 순서 데이터 가져오기
		List<RecipeSeqResponseDTO> recipeSeqResponseDTO = recipeMapper.getRecipeSeq(recipeId);
		
		checkNull(recipeSeqResponseDTO);
		
		// 카테고리 목록 가져오기
		CategoryGetAllResponseDTO categoryGetAllResponseDTO = categoryService.getCategoryAll();
		
		checkNull(categoryGetAllResponseDTO);
	
		recipeEditResponseDTO.setRecipeCategories(categoryGetAllResponseDTO);
		
		return recipeEditResponseDTO;
	}
	
	private void checkNull(Object obj) {
		if (obj == null) {
			throw new CustomCommonException(CommonErrorCode.NOT_FOUND_EXCEPTION);
		}
	}
}
