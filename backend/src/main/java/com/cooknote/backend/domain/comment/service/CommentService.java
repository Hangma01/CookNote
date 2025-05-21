package com.cooknote.backend.domain.comment.service;

import org.springframework.stereotype.Service;

import com.cooknote.backend.domain.comment.dto.request.CommentInsertRequestDTO;

@Service
public interface CommentService {

	void insert(CommentInsertRequestDTO commentInsertRequestDTO);

}
