package com.cooknote.backend.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cooknote.backend.domain.recipe.dto.request.RecipeIngredientRequestDTO;
import com.cooknote.backend.domain.recipe.dto.response.RecipeIngredientResponseDTO;
import com.cooknote.backend.domain.recipe.dto.response.RecipeSeqResponseDTO;


@Mapper
public interface RecipeIngredientMapper {
	
	void save(@Param("ingredients") List<RecipeIngredientRequestDTO> ingredients
			, @Param("recipeId") Long recipeId);
	

	void delete(@Param("recipeId") Long recipeId);
}
