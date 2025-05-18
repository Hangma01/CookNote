package com.cooknote.backend.mappers;

import org.apache.ibatis.annotations.Mapper;

import com.cooknote.backend.domain.recipe.entity.Recipe;


@Mapper
public interface RecipeMapper {

	void recipeSave(Recipe reqRecipe);
}
