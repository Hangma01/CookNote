package com.cooknote.backend.global.infra.aws.s3.dto.request;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public class ConvertRequestDTO {

    private String thumbnail;
    private List<String> images;
}
