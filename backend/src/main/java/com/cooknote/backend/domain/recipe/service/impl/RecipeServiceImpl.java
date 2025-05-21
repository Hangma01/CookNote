package com.cooknote.backend.domain.recipe.service.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cooknote.backend.domain.recipe.dto.request.RecipeSeqRequestDTO;
import com.cooknote.backend.domain.recipe.dto.request.RecipeUpdateRequestDTO;
import com.cooknote.backend.domain.recipe.dto.response.RecipeDetailResponseDTO;
import com.cooknote.backend.domain.recipe.dto.response.RecipeEditResponseDTO;
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

	

	// 레시피 상세 조회
	@Override
	public RecipeDetailResponseDTO getRecipeDetail(String recipeId) {
		
		
		RecipeDetailResponseDTO recipeDetailResponseDTO = recipeMapper.getRecipeDetail(recipeId);
			
		if (recipeDetailResponseDTO == null) {

			throw new CustomCommonException(CommonErrorCode.NOT_FOUND_EXCEPTION);
		}
	
		recipeDetailResponseDTO.setServingLabel(recipeDetailResponseDTO.getServing().getLabel());
		recipeDetailResponseDTO.setDurationLabel(recipeDetailResponseDTO.getDuration().getLabel());
		recipeDetailResponseDTO.setLevelLabel(recipeDetailResponseDTO.getLevel().getLabel());
		
		return recipeDetailResponseDTO;
	}
	
	
	
	// 레시피 저장
	@Override
	@Transactional
	public void recipeSave(Long userId, RecipeSaveRequestDTO recipeSaveRequestDTO) {

		// 이동한 이미지 url들
		List<String> moveImageUrls = new ArrayList<>();
		
		// 썸네일
		String thumbnail =  recipeSaveRequestDTO.getThumbnail();        


		// 이동한 썸네일 url
		String moveThumbnailUrl = s3Service.moveImage(thumbnail, "Recipe/Thumbnails/");
		moveImageUrls.add(moveThumbnailUrl);

		// 레시피 순서 이미지
		List<String> moveRecipeSeqImages = new ArrayList<>();
		List<RecipeSeqRequestDTO> recipeSeqs = recipeSaveRequestDTO.getRecipeSeqs();
		        
        for (RecipeSeqRequestDTO recipeSeq : recipeSeqs) {
        	moveRecipeSeqImages.add(recipeSeq.getImage());
        }
        

        // 이동한 레시피 순서 이미지 url
        List<String> moveRecipeSeqImageUrls =  s3Service.moveImages(moveRecipeSeqImages, "Recipe/SeqImages/");

        
		try {
	        Recipe reqRecipe = Recipe.builder()
					.title(recipeSaveRequestDTO.getTitle())
					.description(recipeSaveRequestDTO.getDescription())
					.thumbnail(moveThumbnailUrl)
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
	        

	        // 레시피 저장
		    recipeMapper.save(reqRecipe);


		    // 재료 저장
		    recipeIngredientMapper.save(recipeSaveRequestDTO.getRecipeIngredients(), reqRecipe.getRecipeId());

        	// 레시피 순서 저장
			for (int i = 0; i < recipeSeqs.size(); i++) {
				String moveRecipeSeqImageUrl = moveRecipeSeqImageUrls.get(i);
				
				recipeSeqs.get(i).setImage(moveRecipeSeqImageUrl);
				moveImageUrls.add(moveRecipeSeqImageUrl);
			}

		    recipeSeqMapper.save(recipeSeqs, reqRecipe.getRecipeId());

		} catch (Exception e) {
	        try {
		        s3Service.deleteImagesFromS3(moveImageUrls);				
			} catch (Exception error) {
				log.warn("s3 이미지 삭제 실패: " + String.join(", ", moveImageUrls));
			}
	        
	       throw new CustomCommonException(CommonErrorCode.UPDATE_EXCEPTION);
		}
	}

	// 레시피 데이터 가져오기 - 수정
	@Override
	public RecipeEditResponseDTO getRecipeForEdit(Long userId, String recipeId) {
		
		// 레시피 데이터 가져오기
		RecipeEditResponseDTO recipeEditResponseDTO = recipeMapper.getRecipeForEdit(recipeId, userId);
		if (recipeEditResponseDTO == null) {

			throw new CustomCommonException(CommonErrorCode.NOT_FOUND_EXCEPTION);
		}
		
		return recipeEditResponseDTO;
	}
	
	// 레시피 업데이트 - 수정
	@Override
	@Transactional
	public void recipeUpdate(Long userId, RecipeUpdateRequestDTO recipeUpdateRequestDTO) {

		// 새로운 썸네일
		String newThumbnail = recipeUpdateRequestDTO.getThumbnail();
		String oldThumbnail = recipeUpdateRequestDTO.getOriginalThumbnail();
        
        // 이전 이미지들
	    List<String> oldImageUrls = new ArrayList<>();
        
	    // 이동할 이미지들
        Map<String, Object> moveImages = new HashMap<>();
        
        // 이동한 이미지 url들
        List<String> moveImageUrls = new ArrayList<>();
        
        
        // 썸네일 변경 여부 체크
        String currentThumbnailUrl = null;

        if(isTempImage(newThumbnail)) {
        	currentThumbnailUrl = s3Service.moveImage(newThumbnail, "Recipe/Thumbnails/");
    		moveImageUrls.add(currentThumbnailUrl);
    		oldImageUrls.add(oldThumbnail);
        } else {
        	currentThumbnailUrl = oldThumbnail;
        }


        // 삭제하지 말아야 할 이미지
        Set<String> keepOldRecipeSeqImages = new HashSet<>();

		// 이동할 레시피 순서 이미지들
		List<String> moveRecipeSeqImages = new ArrayList<>();
        
        // 새로운 레시피 순서 이미지 변경 여부 체크
		List<RecipeSeqRequestDTO> newRecipeSeqs = recipeUpdateRequestDTO.getRecipeSeqs();

        for(RecipeSeqRequestDTO newRecipeSeq : newRecipeSeqs) {
        	
        	String newRecipeSeqImage = newRecipeSeq.getImage();
        	
        	// 이미지 변경 됐는지 체크
        	if(isTempImage(newRecipeSeqImage)) {
        		moveRecipeSeqImages.add(newRecipeSeqImage);
        	} else {
        		keepOldRecipeSeqImages.add(newRecipeSeqImage);
        		moveRecipeSeqImages.add(null);
        	}
        }

        // 이동한 레시피 순서 이미지 url
        List<String> moveRecipeSeqImageUrls =  s3Service.moveImages(moveRecipeSeqImages, "Recipe/SeqImages/");
        
        try {
        	// 레시피 저장
	        Recipe reqRecipe = Recipe.builder()
	        		.recipeId(recipeUpdateRequestDTO.getRecipeId())
					.title(recipeUpdateRequestDTO.getTitle())
					.description(recipeUpdateRequestDTO.getDescription())
					.thumbnail(currentThumbnailUrl)
					.videoId(recipeUpdateRequestDTO.getVideoId())
					.serving(recipeUpdateRequestDTO.getServing())
					.duration(recipeUpdateRequestDTO.getDuration())
					.level(recipeUpdateRequestDTO.getLevel())
					.tip(recipeUpdateRequestDTO.getTip())
					.status(recipeUpdateRequestDTO.getStatus())
					.categoryCuisineId(recipeUpdateRequestDTO.getCategoryCuisineId())
					.categoryPurposeId(recipeUpdateRequestDTO.getCategoryPurposeId())
					.writerId(userId)
					.build();

	        // 레시피 저장
	        log.info(reqRecipe.toString());
		    recipeMapper.update(reqRecipe);

		    // 재료 삭제
		    recipeIngredientMapper.delete(recipeUpdateRequestDTO.getRecipeId());

		    // 재료 저장
		    recipeIngredientMapper.save(recipeUpdateRequestDTO.getRecipeIngredients(), recipeUpdateRequestDTO.getRecipeId());

		    // 레시피 삭제
		    recipeSeqMapper.delete(recipeUpdateRequestDTO.getRecipeId());

        	// 레시피 순서 저장
			for (int i = 0; i < newRecipeSeqs.size(); i++) {
				String moveRecipeSeqImageUrl = moveRecipeSeqImageUrls.get(i);
				
				if(moveRecipeSeqImageUrl != null) {
					newRecipeSeqs.get(i).setImage(moveRecipeSeqImageUrl);
					moveImageUrls.add(moveRecipeSeqImageUrl);
				}
			}

		    recipeSeqMapper.save(newRecipeSeqs, recipeUpdateRequestDTO.getRecipeId());
			
		} catch (Exception e) {
	        try {
		        s3Service.deleteImagesFromS3(moveImageUrls);				
			} catch (Exception error) {
				log.error("s3 이미지 삭제 실패: " + String.join(", ", moveImageUrls));
			}
	        
	       throw new CustomCommonException(CommonErrorCode.UPDATE_EXCEPTION);
		}

        // 기존 레시피 이미지 삭제
	    List<RecipeSeqRequestDTO> oldRecipeSeqs = recipeUpdateRequestDTO.getOriginalRecipeSeqs();
	    
		for (RecipeSeqRequestDTO oldRecipeSeq : oldRecipeSeqs) {
			String oldRecipeSeqImage = oldRecipeSeq.getImage();
			
			if(!keepOldRecipeSeqImages.contains(oldRecipeSeqImage)) {
				oldImageUrls.add(oldRecipeSeqImage);
			}
		}
	    
		s3Service.deleteImagesFromS3(oldImageUrls);
	}
	
	
	private boolean isTempImage(String url) {
	    return url!= null && url.contains("https://cooknote98.s3.ap-northeast-2.amazonaws.com/TempImages/");
	}

}
