package com.cooknote.backend.global.infra.aws.s3.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cooknote.backend.global.infra.aws.s3.dto.request.ConvertRequestDTO;

@Service
public interface S3Service {

	// 외부에서 사용할 public 메서드 파일이 빈 파일인지 검증
	public String upload(MultipartFile image);
	
	// 이미지의 public url을 이용하여 S3에 해당 이미지를 제거하는 메서드이다.
	// getKeyFromImageAddress()를 호출하여 삭제에 필요한 key를 얻는다.
	public void deleteImageFromS3(String imageUrl);
	
	// 여러개의 이미지를 삭제하는 메서드이다.
	public void deleteImagesFromS3(List<String> imageUrls);
	
	public void deleteFolderFromS3(String folderPrefix);
	
	// 이미지 위치를 옮기는 메서드이다.
	public String moveImage(String image, Long userId, String targetFolder, String formatNow);

	// 여러개의 이미지 위치를 옮기는 메서드이다.
	public List<String> moveImages(List<String> images, Long userId, String targetFolder, String formatNow);
}
