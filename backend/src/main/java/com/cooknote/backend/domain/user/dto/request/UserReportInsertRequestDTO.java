package com.cooknote.backend.domain.user.dto.request;

import com.cooknote.backend.domain.user.enums.ReportType;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserReportInsertRequestDTO {
	
	@NotNull
	private ReportType reportType;
	
	private Long recipeId;
	private Long commentId;
	private Long reportedId;
	
	@NotNull
	private Integer categoryReportReasonId;
}
