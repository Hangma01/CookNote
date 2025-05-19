package com.cooknote.backend.domain.recipe.controller;

import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cooknote.backend.domain.recipe.dto.request.RecipeSaveRequestDTO;
import com.cooknote.backend.domain.recipe.dto.response.RecipeEditResponseDTO;
import com.cooknote.backend.domain.recipe.service.RecipeService;
import com.cooknote.backend.global.auth.CustomUserDetails;
import com.cooknote.backend.global.error.exceptionCode.CommonErrorCode;
import com.cooknote.backend.global.error.excption.CustomCommonException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@RequestMapping("/recipe")
@Slf4j
public class RecipeController {
	
	private final RecipeService recipeService;
	
	// 레시피 저장
	@PostMapping("")
	public ResponseEntity<Void> recipeSave(@AuthenticationPrincipal CustomUserDetails customUserDetails 
										   , @Valid @RequestBody RecipeSaveRequestDTO saveRecipeRequestDTO
										   , BindingResult bindingResult) {

		// 유효성 검사 확인
		if (bindingResult.hasErrors()) {
			throw new CustomCommonException(CommonErrorCode.VALIDATION_EXCEPTION);
		}
			
		recipeService.recipeSave(customUserDetails.getUserId(), saveRecipeRequestDTO);
		
		return ResponseEntity.ok().build();
	}
	
	// 레시피 수정
	@GetMapping("/edit/{recipeId}")
	public ResponseEntity<RecipeEditResponseDTO> getRecipeForEdit (@AuthenticationPrincipal CustomUserDetails customUserDetails 
												  				 , @PathVariable("recipeId") String recipeId) {
		
		RecipeEditResponseDTO recipeEditResponseDTO = recipeService.getRecipeForEdit(customUserDetails.getUserId(), recipeId);
		
		return ResponseEntity.ok(recipeEditResponseDTO);
	}
}
