package com.cooknote.backend.domain.recipe.dto.response;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.cooknote.backend.domain.recipe.enums.RecipeDuration;
import com.cooknote.backend.domain.recipe.enums.RecipeLevel;
import com.cooknote.backend.domain.recipe.enums.RecipeServing;
import com.cooknote.backend.domain.recipe.enums.RecipeStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecipeDetailResponseDTO {
    private String title;											// 레시피 제목
    private String description;										// 레시피 요리소개
    private String thumbnail;										// 레시피 썸네일
    private String videoId;											// 동영상 아이디
    
    @JsonIgnore
    private RecipeServing serving;									// 레시피 인원수
    private String servingLabel;									// 레시피 인원수 라벨
    
    @JsonIgnore
    private RecipeDuration duration;								// 레시피 요리시간
    private String durationLabel;									// 레시피 요리시간 라벨
    
    @JsonIgnore
    private RecipeLevel level;	 									// 레시피 난이도 - 0 (쉬움), 1 (보통), 3 (어려움)
    private String levelLabel;										// 레시피 난이도 라벨
    
    private String tip;												// 레시피 요리 팁
    private RecipeStatus status; 									// 레시피 상태 - PRIVATE, PUBLIC, PRIVATE_ADMIN, ELETE.
    private int categoryCuisineId;									// 카테고리 - 요리 종류 (FK)
    private int categoryPurposeId;									// 카테고리 - 요리 목적 (FK)
    private Long writerId;											// 작성자 아이디
    private LocalDateTime createAt;									// 생성 날짜
    
    private String writerNickname;									// 레시피 작성자 닉네임
    private String writerProfileImage;								// 레시피 작성자 프로필 이미지
    
    private Long requesterId;										// 요청자 UserId
    
    private boolean isLiked;										// 좋아요 여부
    private boolean isBookmarked;									// 북마크 여부
    private boolean isAuthor;										// 본인 게시글인지 여부
    
    private List<RecipeIngredientResponseDTO> recipeIngredients;	// 레시피 재료
    private List<RecipeSeqResponseDTO> recipeSeqs;					// 레시피 순서

}
