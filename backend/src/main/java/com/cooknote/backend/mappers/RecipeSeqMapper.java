package com.cooknote.backend.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cooknote.backend.domain.recipe.dto.request.RecipeSeqRequestDTO;

@Mapper
public interface RecipeSeqMapper {

	void recipeSeqSave(@Param("seqs") List<RecipeSeqRequestDTO> seqs,@Param("recipeId") Long recipeId);
}
