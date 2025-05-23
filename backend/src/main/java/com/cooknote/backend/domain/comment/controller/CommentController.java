package com.cooknote.backend.domain.comment.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cooknote.backend.domain.comment.dto.request.CommentInsertRequestDTO;
import com.cooknote.backend.domain.comment.dto.request.CommentUpdateRequestDTO;
import com.cooknote.backend.domain.comment.dto.response.CommentRepliesResponseDTO;
import com.cooknote.backend.domain.comment.dto.response.CommentsResponseDTO;
import com.cooknote.backend.domain.comment.service.CommentService;
import com.cooknote.backend.global.auth.CustomUserDetails;
import com.cooknote.backend.global.error.exceptionCode.CommonErrorCode;
import com.cooknote.backend.global.error.excption.CustomCommonException;
import com.cooknote.backend.global.utils.commonFunction.CommonFunctionUtil;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
@Slf4j
public class CommentController {

	private final CommentService commentService;
	
	// 댓글 목록 조회
	@GetMapping("")
	public ResponseEntity<Page<CommentsResponseDTO>> getComments(@RequestParam("recipeId") Long recipeId
						  			   						   , @RequestParam(value = "page", defaultValue = "0") int page
						  			   						   , @RequestParam(value = "size", defaultValue = "10") int size) {
		
		// Null 체크
		if(CommonFunctionUtil.nullCheck(recipeId)) {
			throw new CustomCommonException(CommonErrorCode.INVALID_STATE_EXCEPTION);
		}
		
		return ResponseEntity.ok(commentService.getComments(recipeId, page, size));
	}
	
	@GetMapping("/replies")
	public ResponseEntity<Page<CommentRepliesResponseDTO>> getReplies(@RequestParam("parentCommentId") Long parentCommentId
				   , @RequestParam(value = "page", defaultValue = "0") int page
				   , @RequestParam(value = "size", defaultValue = "5") int size) {

		// Null 체크
		if(CommonFunctionUtil.nullCheck(parentCommentId)) {
			throw new CustomCommonException(CommonErrorCode.INVALID_STATE_EXCEPTION);
		}
		
		return ResponseEntity.ok(commentService.getCommentReplies(parentCommentId, page, size));
	}
	
	// 댓글 저장
	@PostMapping("")
	public ResponseEntity<Void> commentInsert(@AuthenticationPrincipal CustomUserDetails customUserDetails 
							, @Valid @RequestBody CommentInsertRequestDTO commentInsertRequestDTO
							, BindingResult bindingResult) {
		
		// 유효성 검사 확인
		if (CommonFunctionUtil.validationCheck(bindingResult)) {;
			throw new CustomCommonException(CommonErrorCode.VALIDATION_EXCEPTION);
		}
		
		commentService.commentInsert(customUserDetails.getUserId(), commentInsertRequestDTO);
		
		return ResponseEntity.ok().build();
	}
	
	// 댓글 삭제
	@DeleteMapping("")
	public ResponseEntity<Void> delete(@AuthenticationPrincipal CustomUserDetails customUserDetails,
									   @RequestParam("commentId") Long commentId) {
		
		// 유효성 검사 확인
		if (CommonFunctionUtil.nullCheck(commentId)) {
			throw new CustomCommonException(CommonErrorCode.VALIDATION_EXCEPTION);
		}
		
		commentService.commentDelete(customUserDetails.getUserId(), commentId);
		
		return ResponseEntity.ok().build();
	}
	
	// 댓글 수정
	@PatchMapping("")
	public ResponseEntity<Void> commentUpdate(@AuthenticationPrincipal CustomUserDetails customUserDetails 
									 		, @Valid @RequestBody CommentUpdateRequestDTO commentUpdateRequestDTO
									 		, BindingResult bindingResult) {
	
		// 유효성 검사 확인
		if (CommonFunctionUtil.validationCheck(bindingResult)) {;
			throw new CustomCommonException(CommonErrorCode.VALIDATION_EXCEPTION);
		}
		
		commentService.commentUpdate(customUserDetails.getUserId(), commentUpdateRequestDTO);
		
		return ResponseEntity.ok().build();
	}
}
