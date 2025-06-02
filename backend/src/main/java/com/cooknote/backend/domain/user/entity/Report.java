package com.cooknote.backend.domain.user.entity;

import java.time.LocalDateTime;

import com.cooknote.backend.domain.user.enums.ReportStatus;
import com.cooknote.backend.domain.user.enums.ReportType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Report {
	private Long reportId;
	private ReportType reportType;
	private ReportStatus status;
	private LocalDateTime createAt;
	private LocalDateTime processedAt;
	private Long recipeId;
	private Long commentId;
	private Long reporterId;
	private Long reportedId;
	private int categoryReportReasonId;
}
