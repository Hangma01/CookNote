package com.cooknote.backend.domain.user.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.cooknote.backend.domain.category.entity.CategoryReportReason;
import com.cooknote.backend.domain.user.enums.ReportType;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSacntionResponseDTO {
	private ReportType reportType;
	private String reportLabel;
	private String targetContent;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime reportProcessedAt;
	private List<CategoryReportReason> categoryReportReasons;
}
