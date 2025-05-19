package com.cooknote.backend.domain.recipe.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RecipeEnumLabelResponseDTO {
    private String name;
    private String label;
}
