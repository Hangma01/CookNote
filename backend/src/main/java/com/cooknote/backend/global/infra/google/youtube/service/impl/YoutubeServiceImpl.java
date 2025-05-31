package com.cooknote.backend.global.infra.google.youtube.service.impl;

import java.io.IOException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cooknote.backend.global.error.exceptionCode.CommonErrorCode;
import com.cooknote.backend.global.error.exceptionCode.GoogleErrorCode;
import com.cooknote.backend.global.error.excption.CustomCommonException;
import com.cooknote.backend.global.error.excption.CustomGoogleException;
import com.cooknote.backend.global.infra.google.youtube.dto.response.YoutubeGetThumbnailUrlResponseDTO;
import com.cooknote.backend.global.infra.google.youtube.service.YoutubeService;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;

@Service
public class YoutubeServiceImpl implements YoutubeService {

	@Value("${youtube.api.key}")
    private String apiKey;

	
	@Override
	public YoutubeGetThumbnailUrlResponseDTO getThumbnail(String videoId) {
		
		// JSON 데이터를 처리하기 위한 GsonFactory 객체 생성
        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

        // YouTube 객체를 빌드하여 API에 접근할 수 있는 YouTube 클라이언트 생성
        YouTube youtube = new YouTube.Builder(
                new com.google.api.client.http.javanet.NetHttpTransport(),
                jsonFactory,
                request -> {}
		        ).setApplicationName("CookNote-YouTube-Integration") 
		         .build();
        
        
		try {
	            YouTube.Videos.List videoRequest = youtube.videos().list(Collections.singletonList("snippet"));
	            videoRequest.setKey(apiKey);
	            videoRequest.setId(Collections.singletonList(videoId));
	            videoRequest.setFields("items(snippet(thumbnails))");

	            var videoResponse = videoRequest.execute();
	            var videoItem = videoResponse.getItems().get(0);

	            // 3. 정보 추출
	            var snippet = videoItem.getSnippet();
	            
	            String thumbnailUrl = null;
	            
	            if (snippet.getThumbnails() != null) {
	                if (snippet.getThumbnails().getHigh() != null) 		   thumbnailUrl = snippet.getThumbnails().getHigh().getUrl();
	                else if (snippet.getThumbnails().getMedium() != null)  thumbnailUrl = snippet.getThumbnails().getMedium().getUrl();
	                else if (snippet.getThumbnails().getDefault() != null) thumbnailUrl = snippet.getThumbnails().getDefault().getUrl();
	            }
	            
	            if(thumbnailUrl == null) {
	            	throw new CustomGoogleException(GoogleErrorCode.NOT_FOUND_THUMBNAIL_EXCEPTION);
	            }

	            YoutubeGetThumbnailUrlResponseDTO response = YoutubeGetThumbnailUrlResponseDTO.builder()
	            																		.youtubeThumbnailUrl(thumbnailUrl)
	            																		.build();
	            
	            return response;

		} catch (IOException e) {
			throw new CustomCommonException(CommonErrorCode.API_REQUEST_FAILED_EXCEPTION);
		}
	}
}
