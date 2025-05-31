package com.cooknote.backend.global.infra.google.youtube.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class YoutubeGetThumbnailUrlResponseDTO {
	private String youtubeThumbnailUrl;
}
