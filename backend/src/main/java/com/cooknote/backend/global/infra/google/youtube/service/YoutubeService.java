package com.cooknote.backend.global.infra.google.youtube.service;

import org.springframework.stereotype.Service;

import com.cooknote.backend.global.infra.google.youtube.dto.response.YoutubeGetThumbnailUrlResponseDTO;

@Service
public interface YoutubeService {

	YoutubeGetThumbnailUrlResponseDTO getThumbnail(String videoId);
	
	
}
