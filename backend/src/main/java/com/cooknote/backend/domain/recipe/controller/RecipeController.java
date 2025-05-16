package com.cooknote.backend.domain.recipe.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cooknote.backend.domain.recipe.dto.request.SaveRecipeRequestDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@RequestMapping("/recipe")
@Slf4j
public class RecipeController {
	
	@PostMapping("")
	public ResponseEntity<String> saveRecipe(@RequestParam("title") String title,
	        @RequestParam("description") String description,
	        @RequestParam("thumbnail") MultipartFile thumbnail,
	        @RequestParam("videoId") String videoId,
	        @RequestParam("serving") int serving,
	        @RequestParam("duration") int duration,
	        @RequestParam("level") String level,
	        @RequestParam("tip") String tip,
	        @RequestParam("status") String status,
	        @RequestParam("categoryCuisineId") int categoryCuisineId,
	        @RequestParam("categoryPurposeId") int categoryPurposeId,
	        @RequestParam("ingredients") String ingredientsJson,
	        @RequestParam("recipeSeq") String recipeSeqJson) {
			
		log.info("됐나");
		
		return ResponseEntity.ok("성공");
	}
}
