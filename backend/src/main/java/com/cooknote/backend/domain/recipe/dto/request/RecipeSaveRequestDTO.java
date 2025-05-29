package com.cooknote.backend.domain.recipe.dto.request;

import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.cooknote.backend.domain.recipe.enums.RecipeDuration;
import com.cooknote.backend.domain.recipe.enums.RecipeLevel;
import com.cooknote.backend.domain.recipe.enums.RecipeServing;
import com.cooknote.backend.domain.recipe.enums.RecipeStatus;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RecipeSaveRequestDTO {

	@NotBlank
	@Length(max = 50)
    private String title;											// 레시피 제목
    
	@NotBlank
	@Length(min = 10, max = 400)
	private String description;										// 레시피 요리소개
    
	@NotBlank
	@Length(max = 400)
	private String thumbnail;										// 레시피 썸네일
    
	@Pattern(regexp = "^[a-zA-Z0-9_-]{11}$")
	private String videoId;											// 동영상 아이디
    
	@NotNull
    private RecipeServing serving;									// 레시피 인원수
    
    @NotNull
    private RecipeDuration duration;								// 레시피 요리시간
    
    @NotNull
    private RecipeLevel level;			 							// 레시피 난이도
    
    @Length(max = 400)
    private String tip;												// 레시피 요리 팁
    
    @NotNull
    private RecipeStatus status; 									// 레시피 상태 - PRIVATE, PUBLIC, PRIVATE_ADMIN, ELETE.
    
    @NotNull
    private Integer categoryCuisineId;								// 카테고리 - 요리 종류 (FK)
    
    @NotNull
    private Integer categoryPurposeId;								// 카테고리 - 요리 목적 (FK)
    
    @NotEmpty
    @Size(min = 3)
    @Valid
    private List<RecipeIngredientRequestDTO> recipeIngredients;		// 재료 목록 
    
    @NotEmpty
    @Size(min = 3)
    @Valid
    private List<RecipeSeqRequestDTO> recipeSeqs;					// 레시피 순서 목록
}
