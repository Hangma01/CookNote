package com.cooknote.backend.domain.comment.service;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cooknote.backend.domain.comment.dto.request.CommentInsertRequestDTO;
import com.cooknote.backend.domain.comment.dto.request.CommentUpdateRequestDTO;
import com.cooknote.backend.domain.comment.dto.response.CommentRepliesResponseDTO;
import com.cooknote.backend.domain.comment.dto.response.CommentUserWriteResponseDTO;
import com.cooknote.backend.domain.comment.dto.response.CommentsResponseDTO;
import com.cooknote.backend.global.auth.CustomUserDetails;

import jakarta.validation.Valid;

@Service
public interface CommentService {

	// 댓글 조회
	Page<CommentsResponseDTO> getComments(Long recipeId, int page, int size);
	
	// 리플 조회
	Page<CommentRepliesResponseDTO> getCommentReplies(Long parentCommentId, int page, int size);

	// 댓글 추가
	void commentInsert(Long userId, CommentInsertRequestDTO commentInsertRequestDTO);

	// 댓글 삭제
	void commentDelete(Long userId, Long commentId);
	
	// 댓글 수정
	void commentUpdate(Long userId, CommentUpdateRequestDTO commentUpdateRequestDTO);

	
	// 유저가 작성한 댓글 조회
	Page<CommentUserWriteResponseDTO> getCommentUserWrite(Long userId, int page, int size);

	
	// 리플 총 갯수 조회
	Integer getRepliesCount(Long recipeId);






	

}
