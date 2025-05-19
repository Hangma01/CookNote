package com.cooknote.backend.domain.category.dto.response;

import java.util.List;

import com.cooknote.backend.domain.recipe.dto.response.RecipeEnumLabelResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryGetAllResponseDTO {
	
	private List<CategoryCuisineListResponseDTO> categoryCuisineList;
	private List<CategoryPurposeResponseDTO> categoryPurposeList;
	private List<RecipeEnumLabelResponseDTO> recipeServingList;
	private List<RecipeEnumLabelResponseDTO> recipeDurationList;
	private List<RecipeEnumLabelResponseDTO> recipeLevelList;
}
