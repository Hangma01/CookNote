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
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.util.IOUtils;
import com.cooknote.backend.global.constants.Constans;
import com.cooknote.backend.global.error.exceptionCode.S3ErrorCode;
import com.cooknote.backend.global.error.excption.CustomS3Exception;
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

	// filename을 받아서 파일 확장자가 jpg, jpeg, png 중에 속하는지 검증한다.
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
		String s3FileName = Constans.S3_TEMP_IMAGES_PATH + UUID.randomUUID().toString().substring(0, 10) + originalFilename; 

		InputStream fileInputStream  = image.getInputStream();
		// 이미지를 byte[]로 변환
		byte[] bytes = IOUtils.toByteArray(fileInputStream);

		// metadata 생성
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType(Constans.S3_CONTENT_TYPE + extention);
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
	public void deleteImageFromS3(String imageUrl) {
		String key = getKeyFromImageUrl(imageUrl);

		if (key.startsWith(Constans.S3_DEFAULT_START_WITH)) {
		    return;
		}
		
		try {
			amazonS3.deleteObject(new DeleteObjectRequest(bucketName, key));
		} catch (Exception e) {
			log.warn("삭제 중 실패한 이미지: " + imageUrl.toString());
			throw new CustomS3Exception(S3ErrorCode.IO_EXCEPTION_ON_IMAGE_DELETE);
		}
	}
	
	
	// 여러개의 이미지 삭제
	@Override
	public void deleteImagesFromS3(List<String> imageUrls) {
		try {
			for(String imageUrl : imageUrls) {
				String imageKey = getKeyFromImageUrl(imageUrl);

				if(imageKey != null) {
					amazonS3.deleteObject(new DeleteObjectRequest(bucketName, imageKey));	
				}
			}
		} catch (Exception e) {
			log.warn("삭제 중 실패한 이미지: " + imageUrls.toString());
			throw new CustomS3Exception(S3ErrorCode.IO_EXCEPTION_ON_IMAGE_DELETE);
		}
	}
	
	// 폴더 삭제
	@Override
	public void deleteFolderFromS3(String folderPrefix) {
	    try {
	        List<S3ObjectSummary> objects = amazonS3.listObjects(bucketName, folderPrefix).getObjectSummaries();

	        for (S3ObjectSummary object : objects) {
	            amazonS3.deleteObject(bucketName, object.getKey());
	        }

	        // 마지막으로 "폴더 객체" 자체도 삭제 시도
	        if (!folderPrefix.endsWith("/")) {
	            folderPrefix += "/";
	        }
	        amazonS3.deleteObject(bucketName, folderPrefix);

	    } catch (Exception e) {
	        throw new CustomS3Exception(S3ErrorCode.IO_EXCEPTION_ON_IMAGE_DELETE);
	    }
	}
	
	
	// 이미지의 위치를 옮기는 메서드이다.
	@Override
	public String moveImage(String imageUrl, Long userId, String targetFolder, String formatDate) {
		
		String imageKey = getKeyFromImageUrl(imageUrl);
		String DestinationFolder = targetFolder + userId + "/";
		if(formatDate != null) {
			DestinationFolder += formatDate + "/";
		}
		String imageDestinationKey = getImageDestinationKey(imageKey, DestinationFolder);
		
		try {
			// 이미지 복사
			amazonS3.copyObject(new CopyObjectRequest(bucketName, imageKey, bucketName, imageDestinationKey));
			
			// 기존 위치에 있는 이미지 삭제
			amazonS3.deleteObject(new DeleteObjectRequest(bucketName, imageKey));
	
		} catch (Exception e) {

			try {
				// 복사한 이미지 삭제
				amazonS3.deleteObject(new DeleteObjectRequest(bucketName, imageDestinationKey));
			} catch (Exception ex) {
				log.warn("삭제 실패 위치한 이미지 : " + imageDestinationKey );
			}
			
			throw new CustomS3Exception(S3ErrorCode.IO_EXCEPTION_ON_IMAGE_UPLOAD);
		}
		
		// 복사한 이미지 경로 가져오기
		String moveImageUrl = amazonS3.getUrl(bucketName, imageDestinationKey).toString();
		
		return moveImageUrl;
	}
	
	
	// 여러개의 이미지 위치를 옮기는 메서드이다.
	@Override
	public List<String> moveImages(List<String> imageUrls, Long userId, String targetFolder, String formatDate) {
		
		// 옮겨진 키들 저장
		List<String> moveKeys = new ArrayList<>();
		
		// 옮겨진 키들 url
		List<String> moveImageUrls = new ArrayList<>();
		try {
			for(String imageUrl : imageUrls) {
				if(imageUrl == null) {
					moveImageUrls.add(null);
				} else {
					String imageKey = getKeyFromImageUrl(imageUrl);
					String DestinationFolder = targetFolder + userId + "/" + formatDate + "/";
					String imageDestinationKey = getImageDestinationKey(imageKey, DestinationFolder);
					
				
					// 복사
					amazonS3.copyObject(new CopyObjectRequest(bucketName, imageKey, bucketName, imageDestinationKey));
					moveKeys.add(imageDestinationKey);
					
					// 삭제
					amazonS3.deleteObject(new DeleteObjectRequest(bucketName, imageKey));
							
					// url 가져오기
					String moveImageUrl = amazonS3.getUrl(bucketName, imageDestinationKey).toString();
					moveImageUrls.add(moveImageUrl);
				}
			}
		} catch (Exception e) {
			// 이미지 복원
			for (String moveKey : moveKeys) {
				try {
					amazonS3.deleteObject(new DeleteObjectRequest(bucketName, moveKey));
				} catch (Exception ex) {
					log.warn("삭제 실패 위치" + moveKey + "삭제할 키들" + String.join(", ", moveKey));
				}
			}
			
			throw new CustomS3Exception(S3ErrorCode.IO_EXCEPTION_ON_IMAGE_UPLOAD);
		}
		return moveImageUrls;
	}
	
	
	// 이미지 key 반환
	private String getKeyFromImageUrl(String imageUrl) {
		try {
			URL url = new URL(imageUrl);
			String decodingKey = URLDecoder.decode(url.getPath(), "UTF-8");
			
			return decodingKey.substring(1); // 맨 앞의 '/' 제거
		} catch (MalformedURLException | UnsupportedEncodingException e) {
			throw new CustomS3Exception(S3ErrorCode.IO_EXCEPTION_ON_IMAGE_DELETE);
		}
	}
	
	
	private String getImageDestinationKey(String imageKey, String to) {
		int imageLastIdx = imageKey.lastIndexOf("/");
		String imageFileName = imageKey.substring(imageLastIdx + 1);
		String imageDestinationKey = to + imageFileName;
		
		return imageDestinationKey;
	}


}
