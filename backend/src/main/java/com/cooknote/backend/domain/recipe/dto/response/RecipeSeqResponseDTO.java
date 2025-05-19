package com.cooknote.backend.domain.recipe.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RecipeSeqResponseDTO {
    private Integer step;
    private String description;
    private String image;
}
