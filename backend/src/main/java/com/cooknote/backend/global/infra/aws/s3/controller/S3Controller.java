package com.cooknote.backend.global.infra.aws.s3.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cooknote.backend.global.infra.aws.s3.dto.request.ConvertRequestDTO;
import com.cooknote.backend.global.infra.aws.s3.service.S3Service;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/s3")
public class S3Controller {
	
	private final S3Service s3Service;
	
	@PostMapping("/upload")
	public ResponseEntity<String> s3Upload(@RequestParam("image") MultipartFile image){
		
		String profileImage = s3Service.upload(image);

		return ResponseEntity.ok(profileImage);
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<?> s3Delete(@RequestParam("imageAddress") String imageAddress){
		s3Service.deleteImageFromS3(imageAddress);
	    
		return ResponseEntity.ok().build();
	}
}
