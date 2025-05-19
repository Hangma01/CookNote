package com.cooknote.backend.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cooknote.backend.domain.recipe.dto.response.RecipeEditResponseDTO;
import com.cooknote.backend.domain.recipe.dto.response.RecipeIngredientResponseDTO;
import com.cooknote.backend.domain.recipe.dto.response.RecipeSeqResponseDTO;
import com.cooknote.backend.domain.recipe.entity.Recipe;


@Mapper
public interface RecipeMapper {

	void recipeSave(Recipe reqRecipe);

	RecipeEditResponseDTO getRecipeForEdit(@Param("userId") long userId, @Param("recipeId") String recipeId);

	List<RecipeIngredientResponseDTO> getRecipeIngredient(@Param("recipeId") String recipeId);

	List<RecipeSeqResponseDTO> getRecipeSeq(@Param("recipeId") String recipeId);
}
