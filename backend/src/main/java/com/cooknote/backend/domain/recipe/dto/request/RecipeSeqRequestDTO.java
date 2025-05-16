package com.cooknote.backend.domain.recipe.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeSeqRequestDTO {
    private int step;
    private String description;
    private String image;
}
