package com.cooknote.backend.domain.comment.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cooknote.backend.domain.comment.dto.request.CommentInsertRequestDTO;
import com.cooknote.backend.domain.comment.dto.request.CommentUpdateRequestDTO;
import com.cooknote.backend.domain.comment.dto.response.CommentRepliesResponseDTO;
import com.cooknote.backend.domain.comment.dto.response.CommentUserWriteResponseDTO;
import com.cooknote.backend.domain.comment.dto.response.CommentsResponseDTO;
import com.cooknote.backend.domain.comment.entity.Comment;
import com.cooknote.backend.domain.comment.enums.CommentStatus;
import com.cooknote.backend.domain.comment.service.CommentService;
import com.cooknote.backend.global.auth.CustomUserDetails;
import com.cooknote.backend.global.error.exceptionCode.CommonErrorCode;
import com.cooknote.backend.global.error.excption.CustomCommonException;
import com.cooknote.backend.global.utils.pageable.RequestList;
import com.cooknote.backend.mappers.CommentMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {

	private final CommentMapper commentMapper;
	
	// 댓글 조회
	@Override
	public Page<CommentsResponseDTO> getComments(Long recipeId, int page, int size) {
	
		int offset = page * size;
		List<CommentsResponseDTO> comments = commentMapper.getComments(recipeId, size, offset, CommentStatus.PUBLIC);
		int total = commentMapper.commentsCount(recipeId, CommentStatus.PUBLIC);
		
		return new PageImpl<>(comments, PageRequest.of(page, size), total);
	}
	
	// 리플 조회
	@Override
	public Page<CommentRepliesResponseDTO> getCommentReplies(Long parentCommentId, int page, int size) {
		
		
		int offset = page * size;
		List<CommentRepliesResponseDTO> replies = commentMapper.getCommentReplies(parentCommentId, size, offset, CommentStatus.PUBLIC);
		int total = commentMapper.commentRepliesCount(parentCommentId, CommentStatus.PUBLIC);
		
		return new PageImpl<>(replies, PageRequest.of(page, size), total);
	}
	
	// 댓글 생성
	@Override
	public void commentInsert(Long userId, CommentInsertRequestDTO commentInsertRequestDTO) {
		
		// 대댓글이면 부모 댓글이 삭제 상태인지 확인
		Long parentCommentId = commentInsertRequestDTO.getParentCommentId();
		Long childRecipeId = commentInsertRequestDTO.getRecipeId();
		
		// 대댓글일 경우 체크 리스트
		if(commentInsertRequestDTO.getParentCommentId() != null) {
			
			Comment parentComment = commentMapper.findParentComment(parentCommentId, CommentStatus.PUBLIC);
			
			// 부모 댓글이 있는지 확인 또는 상태가 PUBLIC 아니면 NULL
			if(parentComment == null) {									
				throw new CustomCommonException(CommonErrorCode.NOT_FOUND_EXCEPTION);
			}
			
			// 부모 댓글 마저 자식 댓글인지 확인
			if(parentComment.getParentCommentId() != null) {		
				throw new CustomCommonException(CommonErrorCode.INVALID_STATE_EXCEPTION);
			} 
			
			// 부모 레시피 Id와 자식 레시피 Id 체크
			if(!parentComment.getRecipeId().equals(childRecipeId)) {
				throw new CustomCommonException(CommonErrorCode.INVALID_STATE_EXCEPTION);
			}
		}
		
		commentMapper.commnetInsert(userId, commentInsertRequestDTO);
	}
	
	// 댓글 삭제
	@Override
	public void commentDelete(Long userId, Long commentId) {
		
		Comment comment = commentMapper.findComment(commentId, CommentStatus.PUBLIC);
		
		// 댓글이 있는지 확인 또는 상태가 PUBLIC 아니면 NULL
		if(comment == null) {			
			throw new CustomCommonException(CommonErrorCode.NOT_FOUND_EXCEPTION);
		}
		
		// 본인이 맞는지
		if(!comment.getWriterId().equals(userId)) {
			throw new CustomCommonException(CommonErrorCode.INVALID_STATE_EXCEPTION);
		} 
		
		commentMapper.commentDelete(commentId);
	}

	// 댓글 수정
	@Override
	public void commentUpdate(Long userId, CommentUpdateRequestDTO commentUpdateRequestDTO) {
		
		// 댓글이 수정 가능한 상태인지
		// 본인 댓글이 맞는지
		Long commentId = commentUpdateRequestDTO.getCommentId();
		
		Comment comment = commentMapper.findComment(commentId, CommentStatus.PUBLIC);
		
		// 댓글이 있는지 확인 또는 상태가 PUBLIC 아니면 NULL
		if(comment == null) {					
			throw new CustomCommonException(CommonErrorCode.NOT_FOUND_EXCEPTION);
		}
		
		// 본인이 맞는지
		if(!comment.getWriterId().equals(userId)) {
			throw new CustomCommonException(CommonErrorCode.INVALID_STATE_EXCEPTION);
		} 
		
		
		commentMapper.commentUpdate(commentUpdateRequestDTO);
		
	}

	// 유저가 작성한 댓글 조회
	@Override
	public Page<CommentUserWriteResponseDTO> getCommentUserWrite(Long userId, int page,int size) {
		
		
		int offset = page * size;
		List<CommentUserWriteResponseDTO> comments = commentMapper.getCommentUserWrite(userId, size, offset, CommentStatus.PRIVATE_ADMIN);
		int total = commentMapper.getCommentUserWriteCount(userId);
		
		return new PageImpl<>(comments, PageRequest.of(page, size), total);
	}
}
