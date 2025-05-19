package com.cooknote.backend.global.infra.aws.s3.dto.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ConvertRequestDTO {

    private String thumbnail;
    private List<String> images;
}
