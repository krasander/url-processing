package com.example.urlprocessing.web.rest.dto.mapper;

import com.example.urlprocessing.domain.UrlResult;
import com.example.urlprocessing.web.rest.dto.ResultResponseDto;
import org.springframework.stereotype.Component;

@Component
public class ResultResponseMapper {

    public ResultResponseDto toDto(final UrlResult urlResult) {
        final ResultResponseDto.ResultResponseDtoBuilder builder = ResultResponseDto.builder()
                .url(urlResult.getUrl())
                .nrOfParagraphs(urlResult.getNrOfParagraphs())
                .error(urlResult.getError())
                .createdAt(urlResult.getCreatedAt())
                .startedAt(urlResult.getStartedAt())
                .finishedAt(urlResult.getFinishedAt());

        return builder.build();
    }
}
