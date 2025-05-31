package com.cooknote.backend.domain.user.dto.response;

import java.time.LocalDateTime;

import com.cooknote.backend.domain.user.enums.ReportStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserReportResponseDTO {
	private String reportType;
	private String reportedNickname;
	private String reportReason;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime reportCreateAt;
	private ReportStatus reportStatus;
	private String reportStatusLabel;
}
