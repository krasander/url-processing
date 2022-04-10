package com.example.urlprocessing.web.rest.dto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Builder
@Value
public class ResultResponseDto {

    String url;

    Integer nrOfParagraphs;

    String error;

    LocalDateTime createdAt;

    LocalDateTime startedAt;

    LocalDateTime finishedAt;

}
