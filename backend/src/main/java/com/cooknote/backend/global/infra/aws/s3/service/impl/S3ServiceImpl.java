package com.cooknote.backend.global.infra.aws.s3.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.cooknote.backend.global.error.exceptionCode.S3ErrorCode;
import com.cooknote.backend.global.error.excption.CustomS3Exception;
import com.cooknote.backend.global.infra.aws.s3.dto.request.ConvertRequestDTO;
import com.cooknote.backend.global.infra.aws.s3.service.S3Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class S3ServiceImpl implements S3Service {
	private final AmazonS3 amazonS3;

	@Value("${cloud.aws.s3.bucketName}")
	private String bucketName;

	
	// 외부에서 사용할 public 메서드 파일이 빈 파일인지 검증
	@Override
	public String upload(MultipartFile image) {
		if (image.isEmpty() || Objects.isNull(image.getOriginalFilename())) {
			throw new CustomS3Exception(S3ErrorCode.EMPTY_FILE_EXCEPTION);
		}
		
		// uploadImage를 호출하여 S3에 저장된 이미지의 public url을 반환한다.
		return this.uploadImage(image);
	}

	// validateImageFileExtention() 을 호출하여 확장자 명이 올바른지 확인한다.
	// uploadImageToS3()를 호출하여 이미지를 S3에 업로드하고, S3에 저장된 이미지의 public url을 받아서 서비스 로직에 반환한다.
	private String uploadImage(MultipartFile image) {
		this.validateImageFileExtention(image.getOriginalFilename());
		try {
			return this.uploadImageToS3(image);
		} catch (IOException e) {
			throw new CustomS3Exception(S3ErrorCode.IO_EXCEPTION_ON_IMAGE_UPLOAD);
		}
	}

	// filename을 받아서 파일 확장자가 jpg, jpeg, png, gif 중에 속하는지 검증한다.
	private void validateImageFileExtention(String filename) {
		int lastDotIndex = filename.lastIndexOf(".");
		if (lastDotIndex == -1) {
			throw new CustomS3Exception(S3ErrorCode.NO_FILE_EXTENTION);
		}

		String extention = filename.substring(lastDotIndex + 1).toLowerCase();
		List<String> allowedExtentionList = Arrays.asList("jpg", "jpeg", "png", "webp");

		if (!allowedExtentionList.contains(extention)) {
			throw new CustomS3Exception(S3ErrorCode.INVALID_FILE_EXTENTION);
		}
	}

	// S3에 업로드하는 메서드
	private String uploadImageToS3(MultipartFile image) throws IOException {
		// 원본 파일 명
		String originalFilename = image.getOriginalFilename(); 
		
		// 확장자 명
		String extention = originalFilename.substring(originalFilename.lastIndexOf(".") + 1); 

		// 변경된 파일 명
		String s3FileName = "TempImages/" + UUID.randomUUID().toString().substring(0, 10) + originalFilename; 

		InputStream fileInputStream  = image.getInputStream();
		// 이미지를 byte[]로 변환
		byte[] bytes = IOUtils.toByteArray(fileInputStream);

		// metadata 생성
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType("image/" + extention);
		metadata.setContentLength(bytes.length);
		
		// S3에 요청할 때 사용할 byteInputStream 생성
		// 메모리에 올려서 읽기 떄문에 속도가 빠르다
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

		try {
			// S3로 putObject 할 때 사용할 요청 객체
			// 생성자 : bucket 이름, 파일 명, byteInputStream, metadata
			PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, s3FileName, byteArrayInputStream,
					metadata);
			
			// 아마존 S3에 이미지 데이터를 넣음
			amazonS3.putObject(putObjectRequest);
		} catch (Exception e) {
			throw new CustomS3Exception(S3ErrorCode.PUT_OBJECT_EXCEPTION);
		} finally {
			byteArrayInputStream.close();
			fileInputStream.close();
		}

		return amazonS3.getUrl(bucketName, s3FileName).toString();
	}

	
	// 이미지의 public url을 이용하여 S3에 해당 이미지를 제거하는 메서드이다.
	// getKeyFromImageAddress()를 호출하여 삭제에 필요한 key를 얻는다.
	@Override
	public void deleteImageFromS3(String imageAddress) {
		String key = getKeyFromImageAddress(imageAddress);
		try {
			amazonS3.deleteObject(new DeleteObjectRequest(bucketName, key));
		} catch (Exception e) {
			throw new CustomS3Exception(S3ErrorCode.IO_EXCEPTION_ON_IMAGE_DELETE);
		}
	}
	
	@Override
	public Map<String,Object> convertedGetUrls(Map<String, Object> s3ConvertReq) {

		List<String> copiedKeys = new ArrayList<>();
		
		Map<String, Object> result = new HashMap<>();
		
	
		try {		
			String thumbnailKey = getKeyFromImageAddress((String) s3ConvertReq.get("thumbnail"));
			
			// 썸네일 복사
			int thumbnailLastIdx = thumbnailKey.lastIndexOf('/');
			String thumbnailFileName = thumbnailKey.substring(thumbnailLastIdx + 1);
			String thumbnailDestinationKey = "Recipe/Thumbnail/" + thumbnailFileName;
			
			// 복사
			amazonS3.copyObject(new CopyObjectRequest(bucketName, thumbnailKey, bucketName, thumbnailDestinationKey));
			copiedKeys.add(thumbnailDestinationKey);		
			
			// 삭제
			amazonS3.deleteObject(new DeleteObjectRequest(bucketName, thumbnailKey));
			
			// url 가져오기
			String thumbnailUrl = amazonS3.getUrl(bucketName, thumbnailDestinationKey).toString();
			result.put("thumbnail", thumbnailUrl);
			

			List<String> images = (List<String>) s3ConvertReq.get("images");
			List<String> copiedImageUrls = new ArrayList<>();

			for(String image : images) {
				if(image == null) {
					copiedImageUrls.add(null);
				} else {
					
					String imageKey = getKeyFromImageAddress(image);
					

					int imageLastIdx = imageKey.lastIndexOf('/');
					String imageFileName = imageKey.substring(imageLastIdx + 1);
					String imageDestinationKey = "Recipe/SeqImages/" + imageFileName;

					// 복사
					amazonS3.copyObject(new CopyObjectRequest(bucketName, imageKey, bucketName, imageDestinationKey));
					copiedKeys.add(imageDestinationKey);
					
					// 삭제
					amazonS3.deleteObject(new DeleteObjectRequest(bucketName, imageKey));
					
					// url 가져오기
					String imageUrl = amazonS3.getUrl(bucketName, imageDestinationKey).toString();
					copiedImageUrls.add(imageUrl);
				}
			}
			
			result.put("images", copiedImageUrls);
		} catch (Exception e) {
			for (String copiedKey : copiedKeys) {
				try {
					amazonS3.deleteObject(new DeleteObjectRequest(bucketName, copiedKey));
				} catch (Exception ex) {
					log.error("삭제 실패 위치" + copiedKey + "삭제할 키들" + String.join(", ", copiedKeys));
				}
			}
			
			throw new CustomS3Exception(S3ErrorCode.IO_EXCEPTION_ON_IMAGE_UPLOAD);
		}

		return result;
	}

	private String getKeyFromImageAddress(String imageAddress) {
		try {
			URL url = new URL(imageAddress);
			String decodingKey = URLDecoder.decode(url.getPath(), "UTF-8");
			
			return decodingKey.substring(1); // 맨 앞의 '/' 제거
		} catch (MalformedURLException | UnsupportedEncodingException e) {
			throw new CustomS3Exception(S3ErrorCode.IO_EXCEPTION_ON_IMAGE_DELETE);
		}
	}

	@Override
	public void deleteImagesFromS3(Map<String, Object> s3DeleteReq) {
		try {
			String thumbnailKey = getKeyFromImageAddress((String) s3DeleteReq.get("thumbnail"));
			log.info("deleteImagesFromS3 썸네일: " + thumbnailKey);
			amazonS3.deleteObject(new DeleteObjectRequest(bucketName, thumbnailKey));
			
			List<String> images = (List<String>) s3DeleteReq.get("images");
			for(String image : images) {
				String imageKey = getKeyFromImageAddress(image);
				log.info("deleteImagesFromS3 이미지: " +imageKey);
				amazonS3.deleteObject(new DeleteObjectRequest(bucketName, imageKey));
			}
		} catch (Exception e) {
			throw new CustomS3Exception(S3ErrorCode.IO_EXCEPTION_ON_IMAGE_DELETE);
		}
	}
}
