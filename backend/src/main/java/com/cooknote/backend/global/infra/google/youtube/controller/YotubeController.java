package com.cooknote.backend.global.infra.google.youtube.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cooknote.backend.global.infra.google.youtube.dto.response.YoutubeGetThumbnailUrlResponseDTO;
import com.cooknote.backend.global.infra.google.youtube.service.YoutubeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/youtube")
public class YotubeController {
	
	private final YoutubeService youtubeService;
	
    @GetMapping("")
    public ResponseEntity<YoutubeGetThumbnailUrlResponseDTO> getThumbnail(@RequestParam("videoId") String videoId)  {
    	
        return ResponseEntity.ok(youtubeService.getThumbnail(videoId));

    }
}
