package com.cooknote.backend.domain.recipe.service.impl;


import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.tomcat.util.bcel.Const;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cooknote.backend.domain.recipe.dto.request.RecipeSeqRequestDTO;
import com.cooknote.backend.domain.recipe.dto.request.RecipeUpdateRequestDTO;
import com.cooknote.backend.domain.recipe.dto.response.RecipeBookmarkResponseDTO;
import com.cooknote.backend.domain.recipe.dto.response.RecipeCardResponseDTO;
import com.cooknote.backend.domain.recipe.dto.response.RecipeDetailResponseDTO;
import com.cooknote.backend.domain.recipe.dto.response.RecipeEditResponseDTO;
import com.cooknote.backend.domain.recipe.dto.response.RecipeLikeResponseDTO;
import com.cooknote.backend.domain.recipe.dto.response.RecipePrivateAdminResponseDTO;
import com.cooknote.backend.domain.recipe.dto.response.RecipePrivateResponseDTO;
import com.cooknote.backend.domain.recipe.dto.response.RecipePublicResponseDTO;
import com.cooknote.backend.domain.recipe.dto.response.RecipeRecommnetResponseDTO;
import com.cooknote.backend.domain.recipe.dto.response.RecipeSearchResponseDTO;
import com.cooknote.backend.domain.category.enums.CategoryPurposeEnum;
import com.cooknote.backend.domain.comment.enums.CommentStatus;
import com.cooknote.backend.domain.recipe.dto.request.RecipeSaveRequestDTO;
import com.cooknote.backend.domain.recipe.entity.Recipe;
import com.cooknote.backend.domain.recipe.enums.ConditionalType;
import com.cooknote.backend.domain.recipe.enums.RecipeStatus;
import com.cooknote.backend.domain.recipe.service.RecipeService;
import com.cooknote.backend.domain.user.enums.UserStatus;
import com.cooknote.backend.global.config.AsyncConfig;
import com.cooknote.backend.global.constants.Constans;
import com.cooknote.backend.global.error.exceptionCode.CommonErrorCode;
import com.cooknote.backend.global.error.exceptionCode.RecipeErrorCode;
import com.cooknote.backend.global.error.excption.CustomCommonException;
import com.cooknote.backend.global.error.excption.CustomRecipeException;
import com.cooknote.backend.global.infra.aws.s3.service.S3Service;
import com.cooknote.backend.global.utils.CommonFunctionUtil;
import com.cooknote.backend.mappers.CommentMapper;
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
	private final CommentMapper commentMapper;


	// 레시피 상세 조회
	@Override
	public RecipeDetailResponseDTO getRecipeDetail(Long userId, Long recipeId) {

		RecipeDetailResponseDTO recipeDetailResponseDTO = recipeMapper.getRecipeDetail(userId, recipeId, RecipeStatus.DELETE);
			
		if (recipeDetailResponseDTO == null) {
			throw new CustomCommonException(CommonErrorCode.NOT_FOUND_EXCEPTION);
		}
		
		RecipeStatus recipeStatus = recipeDetailResponseDTO.getStatus();
		Long writerId = recipeDetailResponseDTO.getWriterId();
		
		// 레시피 상태 코드 체크
		if(recipeStatus == RecipeStatus.PRIVATE && !userId.equals(writerId)) {
			throw new CustomCommonException(CommonErrorCode.NOT_FOUND_EXCEPTION);
		} else if(recipeStatus == RecipeStatus.PRIVATE_ADMIN && userId.equals(writerId)) {
			throw new CustomRecipeException(RecipeErrorCode.RECIPE_IS_PRIVATE_ADMIN);
		} else if(recipeStatus == RecipeStatus.PRIVATE_ADMIN && !userId.equals(writerId)) {
			throw new CustomCommonException(CommonErrorCode.NOT_FOUND_EXCEPTION);
		}
	
		
		
		recipeDetailResponseDTO.setServingLabel(recipeDetailResponseDTO.getServing().getLabel());
		recipeDetailResponseDTO.setDurationLabel(recipeDetailResponseDTO.getDuration().getLabel());
		recipeDetailResponseDTO.setLevelLabel(recipeDetailResponseDTO.getLevel().getLabel());
		recipeDetailResponseDTO.setRequesterId(userId);
		
		if(writerId.equals(userId)) {
			recipeDetailResponseDTO.setAuthor(true);
		} else {
			recipeDetailResponseDTO.setAuthor(false);
		}
		
		return recipeDetailResponseDTO;
	}
	
	
	
	// 레시피 저장
	@Override
	@Transactional
	public void recipeSave(Long userId, RecipeSaveRequestDTO recipeSaveRequestDTO) {
		
		// 이동한 이미지 url 저장 리스트
		List<String> moveImageUrls = new ArrayList<>();
		
		// 썸네일 url
		String thumbnail =  recipeSaveRequestDTO.getThumbnail();        

		// 썸네일 Temp -> Recip 폴더로 이동
		LocalDateTime nowTime = LocalDateTime.now().withNano(0);
		String formatNow = CommonFunctionUtil.dateFormat(nowTime);

		String moveThumbnailUrl = s3Service.moveImage(thumbnail, userId, Constans.S3_MOVE_RECIPE_PATH, formatNow);
		moveImageUrls.add(moveThumbnailUrl);

		// 레시피 순서 이미지
		List<String> moveRecipeSeqImages = new ArrayList<>();
		List<RecipeSeqRequestDTO> recipeSeqs = recipeSaveRequestDTO.getRecipeSeqs();
		        
        for (RecipeSeqRequestDTO recipeSeq : recipeSeqs) {
        	moveRecipeSeqImages.add(recipeSeq.getImage());
        }
   
        // 이동한 레시피 순서 이미지 url
        List<String> moveRecipeSeqImageUrls =  s3Service.moveImages(moveRecipeSeqImages, userId, Constans.S3_MOVE_RECIPE_PATH, formatNow);
        
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
					.createAt(nowTime)
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
	        	String folderPrefix = Constans.S3_MOVE_RECIPE_PATH + userId + "/" + formatNow;
		        s3Service.deleteFolderFromS3(folderPrefix);				
			} catch (Exception error) {
				log.warn("s3 이미지 삭제 실패: " + String.join(", ", moveImageUrls));
			}
	        
	       throw new CustomCommonException(CommonErrorCode.UPDATE_EXCEPTION);
		}
	}

	// 레시피 데이터 가져오기 - 수정
	@Override
	public RecipeEditResponseDTO getRecipeForEdit(Long userId, Long recipeId) {
		
		// 레시피 존재 여부 및 본인 확인
		checkSelfRecipeExists(userId, recipeId);

		// 레시피 데이터 가져오기
		RecipeEditResponseDTO recipeEditResponseDTO = recipeMapper.getRecipeForEdit(userId, recipeId, RecipeStatus.DELETE);

		if (recipeEditResponseDTO == null) {
			throw new CustomCommonException(CommonErrorCode.NOT_FOUND_EXCEPTION);
		}

		RecipeStatus recipeStatus = recipeEditResponseDTO.getStatus();
		Long writerId = recipeEditResponseDTO.getWriterId();

		if(recipeStatus == RecipeStatus.PRIVATE && !userId.equals(writerId)) {
			throw new CustomCommonException(CommonErrorCode.NOT_FOUND_EXCEPTION);
		} else if(recipeStatus == RecipeStatus.PRIVATE_ADMIN && userId.equals(writerId)) {
			throw new CustomRecipeException(RecipeErrorCode.RECIPE_IS_PRIVATE_ADMIN);
		} else if(recipeStatus == RecipeStatus.PRIVATE_ADMIN && userId.equals(writerId)) {
			throw new CustomCommonException(CommonErrorCode.NOT_FOUND_EXCEPTION);
		}
		
		
		return recipeEditResponseDTO;
	}
	
	// 레시피 업데이트 - 수정
	@Override
	@Transactional
	public void recipeUpdate(Long userId, RecipeUpdateRequestDTO recipeUpdateRequestDTO) {

		// 레시피 존재 여부 및 본인 확인
		checkSelfRecipeExists(userId, recipeUpdateRequestDTO.getRecipeId());

		// 새로운 썸네일
		String newThumbnail = recipeUpdateRequestDTO.getThumbnail();
		String originalThumbnail = recipeUpdateRequestDTO.getOriginalThumbnail();
        
        // 오리지널 이미지들
	    List<String> originalImageUrls = new ArrayList<>();
        
        
        // 이동한 이미지 url들
        List<String> moveImageUrls = new ArrayList<>();
        
        
        // 썸네일 변경 여부 체크
        String moveThumbnailUrl = null;

        LocalDateTime createAt = recipeMapper.getRecipeCreateAt(userId, recipeUpdateRequestDTO.getRecipeId());		
        String formatCreateAt = CommonFunctionUtil.dateFormat(createAt);

        if(isTempImage(newThumbnail)) {
        	moveThumbnailUrl = s3Service.moveImage(newThumbnail, userId, Constans.S3_MOVE_RECIPE_PATH, formatCreateAt);
    		moveImageUrls.add(moveThumbnailUrl);
    		originalImageUrls.add(originalThumbnail);
        } else {
        	moveThumbnailUrl = originalThumbnail;
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
        List<String> moveRecipeSeqImageUrls =  s3Service.moveImages(moveRecipeSeqImages, userId, Constans.S3_MOVE_RECIPE_PATH, formatCreateAt);

        try {
        	// 레시피 저장
	        Recipe reqRecipe = Recipe.builder()
	        		.recipeId(recipeUpdateRequestDTO.getRecipeId())
					.title(recipeUpdateRequestDTO.getTitle())
					.description(recipeUpdateRequestDTO.getDescription())
					.thumbnail(moveThumbnailUrl)
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
		    recipeMapper.update(reqRecipe, RecipeStatus.PRIVATE_ADMIN);

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
				originalImageUrls.add(oldRecipeSeqImage);
			}
		}
	    
		s3Service.deleteImagesFromS3(originalImageUrls);
	}
	
	// 레시피 삭제
	@Override
	@Transactional
	public void recipeDelete(Long userId, Long recipeId) {

		LocalDateTime createAt = recipeMapper.getRecipeCreateAt(userId, recipeId);		
		if(createAt == null) {
			throw new CustomCommonException(CommonErrorCode.NOT_FOUND_EXCEPTION);
		}

		// 논리 삭제 처리
		recipeMapper.recipeDelete(userId, recipeId, RecipeStatus.DELETE);
		recipeMapper.recipeSeqDelete(recipeId);
		commentMapper.commentDeleteRecipe(recipeId, CommentStatus.DELETE);

		recipeMapper.recipeLikeDelete(userId, recipeId);

		recipeMapper.recipeBookmarkDelete(userId, recipeId);

		// s3 삭제
		String formatCreateAt = CommonFunctionUtil.dateFormat(createAt);
		String folderPrefix = Constans.S3_MOVE_RECIPE_PATH + userId + "/" + formatCreateAt;

		s3Service.deleteFolderFromS3(folderPrefix);
	}
	
	@Override
	@Transactional
	// 레시피 좋아요 생성
	public void recipeLikeInsert(Long userId, Long recipeId) {
		
		// 레시피 존재 여부 확인
		checkRecipeExists(userId, recipeId);
		
		
		recipeMapper.recipeLikeInsert(userId, recipeId);
	}
	
	@Override
	@Transactional
	// 레시피 좋아요 삭제
	public void recipeLikeDelete(Long userId, Long recipeId) {

		// 레시피 존재 여부 확인
		checkRecipeExists(userId, recipeId);
		
		recipeMapper.recipeLikeDelete(userId, recipeId);
	}	
	
	@Override
	@Transactional
	// 북마크 생성
	public void recipeBookmarkInsert(Long userId, Long recipeId) {
		
		// 레시피 존재 여부 확인
		checkRecipeExists(userId, recipeId);
		
		recipeMapper.recipeBookmarkInsert(userId, recipeId);
	}
	
	
	// 북마크 삭제
	@Override
	@Transactional
	public void recipeBookmarkDelete(Long userId, Long recipeId) {
		
		// 레시피 존재 여부 확인
		checkRecipeExists(userId, recipeId);
		
		recipeMapper.recipeBookmarkDelete(userId, recipeId);
	}
	
	// 공개 레시피 조회
	@Override
	public Page<RecipePublicResponseDTO> getRecipePublic(Long accountId, int page, int size) {
		int offset = page * size;
		List<RecipePublicResponseDTO> recipesPublic = recipeMapper.getRecipePublic(accountId, size, offset, RecipeStatus.PUBLIC);
		int total = recipeMapper.recipesPublicCount(accountId, RecipeStatus.PUBLIC);
		return new PageImpl<>(recipesPublic, PageRequest.of(page, size), total);
	}

	
	// 비공개 레시피 조회
	@Override
	public Page<RecipePrivateResponseDTO> getRecipePrivate(Long userId, int page, int size) {
		int offset = page * size;
		List<RecipePrivateResponseDTO> recipesPrivate = recipeMapper.getRecipePrivate(userId, size, offset, RecipeStatus.PRIVATE);
		int total = recipeMapper.recipesPrivateCount(userId, RecipeStatus.PRIVATE);

		return new PageImpl<>(recipesPrivate, PageRequest.of(page, size), total);
	}

	
	// 좋아요한 레시피 조회
	@Override
	public Page<RecipeLikeResponseDTO> getLikeRecipe(Long userId, int page, int size) {
		int offset = page * size;
		List<RecipeLikeResponseDTO> recipesLiked = recipeMapper.getRecipeLike(userId, size, offset, RecipeStatus.PUBLIC);
		int total = recipeMapper.recipeLikeCount(userId, RecipeStatus.PUBLIC);
		
		return new PageImpl<>(recipesLiked, PageRequest.of(page, size), total);
	}

	// 북마크한 레시피 조회
	@Override
	public Page<RecipeBookmarkResponseDTO> getBookmarkRecipe(Long userId, int page, int size) {
		int offset = page * size;
		List<RecipeBookmarkResponseDTO> recipesBookmarked = recipeMapper.getRecipeBookmark(userId, size, offset, RecipeStatus.PUBLIC);
		int total = recipeMapper.recipeBookmarkCount(userId, RecipeStatus.PUBLIC);
		
		return new PageImpl<>(recipesBookmarked, PageRequest.of(page, size), total);
	}
	
	
	// 레시피 검색
	@Override
	public Page<RecipeSearchResponseDTO> getRecipeSearch(String keyword, int categoryCuisineId, int categoryPurposeId, int page, int size, ConditionalType conditionalType) {
		
		List<String> keywords = new ArrayList<>();
		if (keyword != null && !keyword.isBlank()) {
			keywords = Arrays.stream(keyword.trim().split("\\s+"))
			                 .filter(k -> !k.isBlank())
			                 .toList();
		}
		
		int offset = page * size;
		List<RecipeSearchResponseDTO> recipes = recipeMapper.getRecipeSearch(keywords
																		   , categoryCuisineId
																		   , categoryPurposeId
																		   , size
																		   , offset
																		   , RecipeStatus.PUBLIC
																		   , conditionalType
																		   , ConditionalType.POPULAR
																		   , ConditionalType.LATEST
																		   , CommentStatus.PUBLIC
																		   , CommentStatus.PRIVATE_ADMIN
																		   , UserStatus.ACTIVE
																		   , Constans.RECIPE_LIKE_WEIGHT
																		   , Constans.RECIPE_COMMENT_WEIGHT);
		
		int total = recipeMapper.getRecipeSearchCount(keywords, categoryCuisineId, categoryPurposeId, RecipeStatus.PUBLIC);
		
		return new PageImpl<>(recipes, PageRequest.of(page, size), total);
	}
	


	// 레시피 재료 검색
	@Override
	public Page<RecipeSearchResponseDTO> getIngredientSearch(String keyword, int page, int size, ConditionalType conditionalType) {
		
		List<String> ingredients = new ArrayList<>();
		// 재료를 띄워쓰기로 분리하여 재료로 만들기 \s는 한개 이상의 띄워쓰기
		if(keyword != null) {
			ingredients = Arrays.asList(keyword.trim().split("\\s+"));
		}
		int ingredientCount = ingredients.size();
		
		int offset = page * size;
		List<RecipeSearchResponseDTO> recipes = recipeMapper.getIngredientSearch(ingredients
																			   , ingredientCount
																			   , size
																			   , offset
																			   , RecipeStatus.PUBLIC
																			   , conditionalType
																			   , ConditionalType.POPULAR
																			   , ConditionalType.LATEST
																			   , CommentStatus.PUBLIC
																			   , UserStatus.ACTIVE
																			   , Constans.RECIPE_LIKE_WEIGHT
																			   , Constans.RECIPE_COMMENT_WEIGHT);
	
		int total = recipeMapper.getIngredientSearchCount(ingredients, ingredientCount, RecipeStatus.PUBLIC);
		
		return new PageImpl<>(recipes, PageRequest.of(page, size), total);
	}
	
	
	// 추천 레시피
	@Override
	public List<RecipeRecommnetResponseDTO> getRecommentRecipe() {
		List<RecipeRecommnetResponseDTO> recommentRecipeList = recipeMapper.getRecommentRecipe(RecipeStatus.PUBLIC
																						     , Constans.GET_RECIPE_LIMIT
																						     , CommentStatus.PUBLIC
																						     , UserStatus.ACTIVE
																						     , Constans.RECIPE_LIKE_WEIGHT
																						     , Constans.RECIPE_COMMENT_WEIGHT);
		int resultSize = Math.min(recommentRecipeList.size(), Constans.RECOMMENT_RECIPE_MIN_VALUE);
		Collections.shuffle(recommentRecipeList);
		
		return recommentRecipeList.subList(0, resultSize);
	}
	
	// 베스트 레시피 가져오기
	@Override
	public List<RecipeCardResponseDTO> getBestRecipe() {
		List<RecipeCardResponseDTO> bestRecipeList = recipeMapper.getBestRecipe(RecipeStatus.PUBLIC
																			  , Constans.BEST_RECIPE_MIN_VALUE
																			  , CommentStatus.PUBLIC
																			  , UserStatus.ACTIVE
																			  , Constans.RECIPE_LIKE_WEIGHT
																	          , Constans.RECIPE_COMMENT_WEIGHT);
		
		return bestRecipeList;
	}
	
	// 혼밥 레시피 가져오기
	@Override
	public List<RecipeRecommnetResponseDTO> getSoloRecipe() {
		List<RecipeRecommnetResponseDTO> soloRecitList = recipeMapper.getSoloRecipe(RecipeStatus.PUBLIC
																				  , CategoryPurposeEnum.SOLO_MEAL.getCode()
																				  , Constans.GET_RECIPE_LIMIT
																				  , CommentStatus.PUBLIC
																				  , UserStatus.ACTIVE
																				  , Constans.RECIPE_LIKE_WEIGHT
																				  , Constans.RECIPE_COMMENT_WEIGHT);
		
		int resultSize = Math.min(soloRecitList.size() ,Constans.SOLO_RECIPE_MIN_VALUE);
		Collections.shuffle(soloRecitList);
		
		return soloRecitList.subList(0, resultSize);
	}
	
	// 최신 레시피 가져오기
	@Override
	public List<RecipeCardResponseDTO> getRecentRecipe() {
		List<RecipeCardResponseDTO> recentRecipeList = recipeMapper.getRecentRecipe(RecipeStatus.PUBLIC
																				  , Constans.RECENT_RECIPE_MIN_VALUE
																				  , CommentStatus.PUBLIC
																				  , UserStatus.ACTIVE);

		return recentRecipeList;
	}
	

	// 팔로우한 유저들의 전체 게시글
	@Override
	public Page<RecipeSearchResponseDTO> getRecipesOfFollowingUsers(Long userId, int page, int size) {
		int offset = page * size;
		List<RecipeSearchResponseDTO> recipesLiked = recipeMapper.getRecipesOfFollowingUsers(userId, size, offset, RecipeStatus.PUBLIC, CommentStatus.PUBLIC, UserStatus.ACTIVE);
		int total = recipeMapper.getRecipesOfFollowingUsersCount(userId, RecipeStatus.PUBLIC);
		
		return new PageImpl<>(recipesLiked, PageRequest.of(page, size), total);
	}


	// 팔로우한 특정 유저의 게시글
	@Override
	public Page<RecipeSearchResponseDTO> getRecipesByFollowingUser(Long userId, Long followingId, int page, int size) {
		int offset = page * size;
		List<RecipeSearchResponseDTO> recipesLiked = recipeMapper.getRecipesByFollowingUser(userId, followingId, size, offset, RecipeStatus.PUBLIC, CommentStatus.PUBLIC, UserStatus.ACTIVE);
		int total = recipeMapper.getRecipesByFollowingUserCount(userId, followingId, RecipeStatus.PUBLIC);
		
		return new PageImpl<>(recipesLiked, PageRequest.of(page, size), total);
	}
	
	

	// 레시피 좋아요한 카운트 가져오기
	@Override
	public Integer getRecipeLikeCount(Long recipeId) {
		
		return recipeMapper.getRecipeLikeCount(recipeId, UserStatus.ACTIVE);
	}

	
		
	// 임시 파일 체크
	private boolean isTempImage(String url) {
	    return url!= null && url.contains(Constans.S3_TEMP_IMAGES_FULL_PATH);
	}
	

	
	// 레시피 존재 여부 체크
	private void checkRecipeExists(Long userId, Long recipeId) {
		Recipe recipe =  recipeMapper.checkRecipe(recipeId, RecipeStatus.PUBLIC);

		if(recipe == null) {
			throw new CustomCommonException(CommonErrorCode.NOT_FOUND_EXCEPTION);
		}
		
		if(recipe.getWriterId().equals(userId)) {
			throw new CustomRecipeException(RecipeErrorCode.RECIPE_SELF_USER_EXCEPTION);
		}
	}
	
	// 본인 레시피 인지 체크
	private void checkSelfRecipeExists(Long userId, Long recipeId) {
		boolean result =  recipeMapper.checkSelfRecipeExists(userId, recipeId, RecipeStatus.PUBLIC, RecipeStatus.PRIVATE);
		
		if(!result) {
			throw new CustomCommonException(CommonErrorCode.NOT_FOUND_EXCEPTION);
		}
	}





}
