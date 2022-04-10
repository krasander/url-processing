package com.example.urlprocessing.web.rest.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class ProcessResponseDto {

    @JsonCreator
    public ProcessResponseDto(@JsonProperty("url") String url) {
        this.url = url;
    }

    String url;
}
