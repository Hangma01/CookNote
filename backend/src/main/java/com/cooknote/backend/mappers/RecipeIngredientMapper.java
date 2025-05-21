package com.cooknote.backend.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cooknote.backend.domain.recipe.dto.request.RecipeIngredientRequestDTO;


@Mapper
public interface RecipeIngredientMapper {
	
	void save(@Param("ingredients") List<RecipeIngredientRequestDTO> ingredients
			, @Param("recipeId") Long recipeId);
	

	void delete(Long recipeId);
}
