package com.cooknote.backend.global.infra.aws.s3.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cooknote.backend.global.infra.aws.s3.service.S3Service;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/s3")
public class S3Controller {
	
	private final S3Service s3Service;
	
	@PostMapping("/upload")
	public ResponseEntity<?> s3Upload(@RequestPart(value = "image", required = false) MultipartFile image){
		String profileImage = s3Service.upload(image);
		return ResponseEntity.ok(profileImage);
	}
	
	@GetMapping("/s3/delete")
	public ResponseEntity<?> s3delete(@RequestParam String addr){
		s3Service.deleteImageFromS3(addr);
	    return ResponseEntity.ok(null);
	}
}
