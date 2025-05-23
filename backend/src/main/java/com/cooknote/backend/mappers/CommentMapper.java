package com.cooknote.backend.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cooknote.backend.domain.comment.dto.request.CommentInsertRequestDTO;
import com.cooknote.backend.domain.comment.dto.request.CommentUpdateRequestDTO;
import com.cooknote.backend.domain.comment.dto.response.CommentRepliesResponseDTO;
import com.cooknote.backend.domain.comment.dto.response.CommentsResponseDTO;
import com.cooknote.backend.domain.comment.entity.Comment;
import com.cooknote.backend.domain.comment.enums.CommentStatus;

@Mapper
public interface CommentMapper {

	// 댓글 조회
	List<CommentsResponseDTO> getComments(@Param("recipeId") Long recipeId
							, @Param("size") int size
							, @Param("offset") int offset 
							, @Param("statusPublic") CommentStatus statusPublic);

	// 댓글 토탈 갯수
	int commentsCount(@Param("recipeId") Long recipeId 
					, @Param("statusPublic") CommentStatus statusPublic);
	
	// 리플 조회
	List<CommentRepliesResponseDTO> getCommentReplies(@Param("parentCommentId") Long parentCommentId 
													, @Param("size") int size
													, @Param("offset") int offset 
													, @Param("statusPublic") CommentStatus statusPublic);
	
	// 리플 토탈 갯수
	int commentRepliesCount(@Param("parentCommentId")  Long parentCommentId
						  , @Param("statusPublic") CommentStatus statusPublic);
	
	
	// 댓글 추가 - 부모 댓글 조회
	Comment findParentComment(@Param("parentCommentId") Long parentCommentId
							, @Param("statusPublic") CommentStatus statusPublic);

	
	// 댓글 추가
	void commnetInsert(@Param("userId") Long userId
					 , @Param("insertDTO") CommentInsertRequestDTO commentInsertRequestDTO);

	
	// 댓글 삭제
	void commentDelete(@Param("commentId") Long commentId
					 , @Param("commentStatus") CommentStatus delete);
	
	

	// 댓글 수정 - 기존 정보 조회
	Comment findComment(@Param("commentId") Long commentId
					  , @Param("statusPublic") CommentStatus statusPublic);


	// 댓글 수정
	void commentUpdate(@Param("updateDTO") CommentUpdateRequestDTO commentUpdateRequestDTO);

}
