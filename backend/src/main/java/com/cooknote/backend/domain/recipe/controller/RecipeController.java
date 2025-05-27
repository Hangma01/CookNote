package com.cooknote.backend.domain.recipe.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cooknote.backend.domain.recipe.dto.request.RecipeBookmarkRequestDTO;
import com.cooknote.backend.domain.recipe.dto.request.RecipeLikeRequestDTO;
import com.cooknote.backend.domain.recipe.dto.request.RecipeSaveRequestDTO;
import com.cooknote.backend.domain.recipe.dto.request.RecipeUpdateRequestDTO;
import com.cooknote.backend.domain.recipe.dto.response.RecipeBookmarkResponseDTO;
import com.cooknote.backend.domain.recipe.dto.response.RecipeDetailResponseDTO;
import com.cooknote.backend.domain.recipe.dto.response.RecipeEditResponseDTO;
import com.cooknote.backend.domain.recipe.dto.response.RecipeLikeResponseDTO;
import com.cooknote.backend.domain.recipe.dto.response.RecipePrivateResponseDTO;
import com.cooknote.backend.domain.recipe.dto.response.RecipePublicResponseDTO;
import com.cooknote.backend.domain.recipe.dto.response.RecipeSearchResponseDTO;
import com.cooknote.backend.domain.recipe.service.RecipeService;
import com.cooknote.backend.global.auth.CustomUserDetails;
import com.cooknote.backend.global.error.exceptionCode.CommonErrorCode;
import com.cooknote.backend.global.error.exceptionCode.RecipeErrorCode;
import com.cooknote.backend.global.error.excption.CustomCommonException;
import com.cooknote.backend.global.error.excption.CustomRecipeException;
import com.cooknote.backend.global.utils.common.CommonFunctionUtil;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@RequestMapping("/recipe")
@Slf4j
public class RecipeController {
	
	private final RecipeService recipeService;
	
	// 공개 레시피 조회
	@GetMapping("/public/host")
	public ResponseEntity<Page<RecipePublicResponseDTO>> getHostRecipePublic(@RequestParam("hostId") Long hostId
			 															   , @RequestParam(value = "page", defaultValue = "0") int page
			 															   , @RequestParam(value = "size", defaultValue = "10") int size) {
		
		// Null 체크
		if(CommonFunctionUtil.nullCheck(hostId)) {
			throw new CustomCommonException(CommonErrorCode.INVALID_STATE_EXCEPTION);
		}	
		
		return ResponseEntity.ok(recipeService.getRecipePublic(hostId, page, size));
	}
	
	// 공개 레시피 조회 - 본인
	@GetMapping("/public")
	public ResponseEntity<Page<RecipePublicResponseDTO>> getRecipePublic(@AuthenticationPrincipal CustomUserDetails customUserDetails
																	   , @RequestParam(value = "page", defaultValue = "0") int page
																	   , @RequestParam(value = "size", defaultValue = "10") int size) {
			
		return ResponseEntity.ok(recipeService.getRecipePublic(customUserDetails.getUserId(), page, size));
	}
	
	// 비공개 레시피 조회
	@GetMapping("/private")
	public ResponseEntity<Page<RecipePrivateResponseDTO>> getRecipePrivate(@AuthenticationPrincipal CustomUserDetails customUserDetails
																		 , @RequestParam(value = "page", defaultValue = "0") int page
																		 , @RequestParam(value = "size", defaultValue = "10") int size) {
		
		
		
		return ResponseEntity.ok(recipeService.getRecipePrivate(customUserDetails.getUserId(), page, size));
	}
		
	// 레시피 상세 조회
	@GetMapping("/{recipeId}")
	public ResponseEntity<RecipeDetailResponseDTO> getRecipeDetail(@AuthenticationPrincipal CustomUserDetails customUserDetails
																 , @PathVariable("recipeId") Long recipeId) {
		
		Long userId = null;
		
		if (customUserDetails != null) {
		    userId = customUserDetails.getUserId();
		}
		
		// Null 체크
		if(CommonFunctionUtil.nullCheck(recipeId)) {
			throw new CustomCommonException(CommonErrorCode.INVALID_STATE_EXCEPTION);
		}
		
		RecipeDetailResponseDTO recipeDetailResponseDTO =  recipeService.getRecipeDetail(userId, recipeId);
		
		return ResponseEntity.ok(recipeDetailResponseDTO);
	}
	
	// 레시피 생성
	@PostMapping("")
	public ResponseEntity<Void> recipeSave(@AuthenticationPrincipal CustomUserDetails customUserDetails 
										 , @Valid @RequestBody RecipeSaveRequestDTO saveRecipeRequestDTO
										 , BindingResult bindingResult) {
		
		// 유효성 검사 확인
		if (CommonFunctionUtil.validationCheck(bindingResult)) {
			throw new CustomCommonException(CommonErrorCode.VALIDATION_EXCEPTION);
		}
		
		recipeService.recipeSave(customUserDetails.getUserId(), saveRecipeRequestDTO);
		
		return ResponseEntity.ok().build();
	}
	
	// 레시피 수정 요청
	@GetMapping("/edit/{recipeId}")
	public ResponseEntity<RecipeEditResponseDTO> getRecipeForEdit(@AuthenticationPrincipal CustomUserDetails customUserDetails 
												  				, @PathVariable("recipeId") Long recipeId) {
		
		// Null 체크
		if(CommonFunctionUtil.nullCheck(recipeId)) {
			throw new CustomCommonException(CommonErrorCode.INVALID_STATE_EXCEPTION);
		}
		
		RecipeEditResponseDTO recipeEditResponseDTO = recipeService.getRecipeForEdit(customUserDetails.getUserId(), recipeId);
		
		return ResponseEntity.ok(recipeEditResponseDTO);
	}
	
	// 레시피 수정 저장
	@PatchMapping("/edit")
	public ResponseEntity<Void> recipeUpdate(@AuthenticationPrincipal CustomUserDetails customUserDetails 
										   , @Valid @RequestBody RecipeUpdateRequestDTO recipeUpdateRequestDTO
										   , BindingResult bindingResult) {

		// 유효성 검사 확인
		if (CommonFunctionUtil.validationCheck(bindingResult)) {;
			throw new CustomCommonException(CommonErrorCode.VALIDATION_EXCEPTION);
		}
		
		recipeService.recipeUpdate(customUserDetails.getUserId(), recipeUpdateRequestDTO);
		
		
		return ResponseEntity.ok().build();
	}
	
	// 레시피 삭제
	@DeleteMapping("")
	public ResponseEntity<Void> recipeUpdate(@AuthenticationPrincipal CustomUserDetails customUserDetails 
			   							   , @RequestParam("recipeId") Long recipeId) {
		

		// Null 체크
		if(CommonFunctionUtil.nullCheck(recipeId)) {
			throw new CustomCommonException(CommonErrorCode.INVALID_STATE_EXCEPTION);
		}

		recipeService.recipeDelete(customUserDetails.getUserId(), recipeId);
	
		return ResponseEntity.ok().build();
	}	
	
	// 좋아요한 공개 레시피 데이터 가져오기
	@GetMapping("/like")
	public ResponseEntity<Page<RecipeLikeResponseDTO>> getLikedRecipe(@AuthenticationPrincipal CustomUserDetails customUserDetails
			   														   	   , @RequestParam(value = "page", defaultValue = "0") int page
			   														   	   , @RequestParam(value = "size", defaultValue = "10") int size) {

		return ResponseEntity.ok(recipeService.getLikeRecipe(customUserDetails.getUserId(), page, size));
	}
	
	// 레시피 좋아요 추가
	@PostMapping("/like")
	public ResponseEntity<Void> recipeLikeInsert(@AuthenticationPrincipal CustomUserDetails customUserDetails 
											   , @Valid @RequestBody RecipeLikeRequestDTO recipeLikeRequestDTO
											   , BindingResult bindingResult) {

		// 유효성 검사 확인
		if (CommonFunctionUtil.validationCheck(bindingResult)) {
			throw new CustomCommonException(CommonErrorCode.VALIDATION_EXCEPTION);
		}

		recipeService.recipeLikeInsert(customUserDetails.getUserId(), recipeLikeRequestDTO.getRecipeId());
	
		return ResponseEntity.ok().build();
	}	
	
	// 레시피 좋아요 삭제
	@DeleteMapping("/like")
	public ResponseEntity<Void> recipeLikeDelete(@AuthenticationPrincipal CustomUserDetails customUserDetails 
			   								   , @RequestParam("recipeId") Long recipeId) {

		// Null 체크
		if(CommonFunctionUtil.nullCheck(recipeId)) {
			throw new CustomCommonException(CommonErrorCode.INVALID_STATE_EXCEPTION);
		}

		recipeService.recipeLikeDelete(customUserDetails.getUserId(), recipeId);
		
		return ResponseEntity.ok().build();
	}
	
	
	// 북마크한 공개 레시피 데이터 가져오기
	@GetMapping("/bookmark")
	public ResponseEntity<Page<RecipeBookmarkResponseDTO>> getBookmarkRecipePublic(@AuthenticationPrincipal CustomUserDetails customUserDetails
			   														   , @RequestParam(value = "page", defaultValue = "0") int page
			   														   , @RequestParam(value = "size", defaultValue = "10") int size) {

		return ResponseEntity.ok(recipeService.getBookmarkRecipe(customUserDetails.getUserId(), page, size));
	}
	
	// 레시피 북마크 추가
	@PostMapping("/bookmark")
	public ResponseEntity<Void> recipeBookmarkInsert(@AuthenticationPrincipal CustomUserDetails customUserDetails 
											       , @Valid @RequestBody RecipeBookmarkRequestDTO recipeBookmarkRequestDTO
											       , BindingResult bindingResult) {
		
		// 유효성 검사 확인
		if (CommonFunctionUtil.validationCheck(bindingResult)) {;
			throw new CustomCommonException(CommonErrorCode.VALIDATION_EXCEPTION);
		}
		
		recipeService.recipeBookmarkInsert(customUserDetails.getUserId(), recipeBookmarkRequestDTO.getRecipeId());
		
		return ResponseEntity.ok().build();
	}	
	
	// 레시피 북마크 삭제
	@DeleteMapping("/bookmark")
	public ResponseEntity<Void> recipeBookmarkDelete(@AuthenticationPrincipal CustomUserDetails customUserDetails 
												   , @RequestParam("recipeId") Long recipeId) {

		// Null 체크
		if(CommonFunctionUtil.nullCheck(recipeId)) {
			throw new CustomCommonException(CommonErrorCode.INVALID_STATE_EXCEPTION);
		}
		
		recipeService.recipeBookmarkDelete(customUserDetails.getUserId(), recipeId);
		
		return ResponseEntity.ok().build();
	}
	
	
	// 레시피 검색 조회
	@GetMapping("/search")
	public ResponseEntity<Page<RecipeSearchResponseDTO>> getRecipeSearch(@RequestParam(value = "keyword", required = false) String keyword
										   , @RequestParam(value = "categoryCuisineId", defaultValue = "0") int categoryCuisineId
										   , @RequestParam(value = "categoryPurposeId", defaultValue = "0") int categoryPurposeId
										   , @RequestParam(value = "page", defaultValue = "0") int page
										   , @RequestParam(value = "size", defaultValue = "20") int size) {
		
		return ResponseEntity.ok(recipeService.getRecipeSearch(keyword, categoryCuisineId, categoryPurposeId, page, size));
	}
	
	
	// 레시피 재료 검색 조회
	@GetMapping("/search/ingredient")
	public ResponseEntity<Page<RecipeSearchResponseDTO>> getIngredientSearch(@RequestParam(value = "keyword", required = false) String keyword
										   								   , @RequestParam(value = "page", defaultValue = "0") int page
										   								   , @RequestParam(value = "size", defaultValue = "20") int size) {
		
		
		return ResponseEntity.ok(recipeService.getIngredientSearch(keyword, page, size));
	}
	
	
	// 유저가 팔로우한 유저들의 전체 게시글
	@GetMapping("/following")
	public ResponseEntity<Page<RecipeSearchResponseDTO>> getRecipesOfFollowingUsers(@AuthenticationPrincipal CustomUserDetails customUserDetails 
																				  , @RequestParam(value = "page", defaultValue = "0") int page
																				  , @RequestParam(value = "size", defaultValue = "20") int size) {
	
		
		
		return ResponseEntity.ok(recipeService.getRecipesOfFollowingUsers(customUserDetails.getUserId(), page, size));
	}
	
	// 유저가 팔로우한 특정 유저의 게시글
	@GetMapping("/following/{followingId}")
	public ResponseEntity<Page<RecipeSearchResponseDTO>> getRecipesByFollowingUser(@AuthenticationPrincipal CustomUserDetails customUserDetails 
																				 , @PathVariable("followingId") Long followingId
																			     , @RequestParam(value = "page", defaultValue = "0") int page
																			     , @RequestParam(value = "size", defaultValue = "20") int size) {
	
		
		return ResponseEntity.ok(recipeService.getRecipesByFollowingUser(customUserDetails.getUserId(), followingId, page, size));
	}

}
