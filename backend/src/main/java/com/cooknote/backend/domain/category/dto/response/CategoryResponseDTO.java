package com.cooknote.backend.domain.category.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponseDTO {
	private List<CategoryCuisineListResponseDTO> categoryCuisineList;
	private List<CategoryPurposeResponseDTO> categoryPurposeList;
}
