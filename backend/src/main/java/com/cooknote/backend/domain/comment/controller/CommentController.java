package com.cooknote.backend.domain.comment.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooknote.backend.domain.comment.dto.request.CommentInsertRequestDTO;
import com.cooknote.backend.domain.comment.service.CommentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

	private final CommentService commentService;
	
	// 댓글 목록 조회
//	@GetMapping("")
//	public void select(int recipeId) {
//		
//	}
	
	// 댓글 삽입
	@PostMapping("")
	public void insert(@RequestBody CommentInsertRequestDTO commentInsertRequestDTO) {
		
		commentService.insert(commentInsertRequestDTO);
	}
	
//	// 댓글 삭제
//	@DeleteMapping("")
//	public int delete(@RequestBody fff) {
//		
//	}
//	
//	// 댓글 수정
//	@PatchMapping("")
//	public int update(@RequestBody fff) {
//		
//	}
}
