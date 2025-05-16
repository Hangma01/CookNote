package com.cooknote.backend.domain.recipe.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeIngredientRequestDTO {
    private String name;
    private String quantity;
    private String remark;
}
