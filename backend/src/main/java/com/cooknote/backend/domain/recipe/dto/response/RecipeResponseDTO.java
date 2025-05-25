package com.cooknote.backend.domain.recipe.dto.response;

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
public class RecipeResponseDTO {
	private String title;			// 레시피 제목
	private String description;		// 레시피 요리소개
    private String thumbnail;		// 레시피 썸네일
	    
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd")
	private Date createAt;			// 생성 날짜
}




// 딱 한명만 검색할 때
//select 
//U.NICKNAME,
//U.PROFILE_IMAGE AS 이미지,
//SUM(CASE WHEN R.STATUS = 'PUBLIC' THEN 1 ELSE 0 END) AS '공개 게시글 수',
//SUM(CASE WHEN R.STATUS IN ('PRIVATE', 'PRIVATE_ADMIN') THEN 1 ELSE 0 END) AS '비공개 게시글 수', 
//(SELECT COUNT(FOLLOWING_ID) FROM FOLLOW WHERE FOLLOWING_ID = 1) AS 팔로잉,
//(SELECT COUNT(FOLLOWER_ID) FROM FOLLOW WHERE FOLLOWER_ID = 1) AS 팔로워
//FROM USER U
//LEFT JOIN RECIPE R ON U.USER_ID = R.WRITER_ID
//WHERE USER_ID = 1
//GROUP BY USER_ID;


// 여러명 검색할떄 
//SELECT 
//  U.USER_ID,
//  U.NICKNAME,
//  U.PROFILE_IMAGE AS profile_image,
//  R.PUBLIC_COUNT,
//  R.PRIVATE_COUNT,
//  F.FOLLOWING_COUNT,
//  F.FOLLOWER_COUNT
//FROM USER U
//

//LEFT JOIN (
//		  SELECT 
//		    WRITER_ID,
//		    SUM(CASE WHEN STATUS = 'PUBLIC' THEN 1 ELSE 0 END) AS PUBLIC_COUNT,
//		    SUM(CASE WHEN STATUS IN ('PRIVATE', 'PRIVATE_ADMIN') THEN 1 ELSE 0 END) AS PRIVATE_COUNT
//		  FROM RECIPE
//		  GROUP BY WRITER_ID
//		) R ON U.USER_ID = R.WRITER_ID
//
//		-- 팔로우 정보 서브쿼리
//		LEFT JOIN (
//		  SELECT 
//		    U1.USER_ID,
//		    (SELECT COUNT(*) FROM FOLLOW WHERE FOLLOWER_ID = U1.USER_ID) AS FOLLOWING_COUNT,
//		    (SELECT COUNT(*) FROM FOLLOW WHERE FOLLOWING_ID = U1.USER_ID) AS FOLLOWER_COUNT
//		  FROM USER U1
//		) F ON U.USER_ID = F.USER_ID
//
//		WHERE U.USER_ID = 3;