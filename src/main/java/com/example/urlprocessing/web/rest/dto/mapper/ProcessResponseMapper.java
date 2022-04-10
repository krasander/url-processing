package com.example.urlprocessing.web.rest.dto.mapper;

import com.example.urlprocessing.config.ControllerConfiguration;
import com.example.urlprocessing.web.rest.dto.ProcessResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProcessResponseMapper {

    private final ControllerConfiguration controllerConfiguration;

    public ProcessResponseDto toDto(final UUID uuid) {
        final ProcessResponseDto.ProcessResponseDtoBuilder builder = ProcessResponseDto.builder()
                .url(controllerConfiguration.getRequestMapping() + controllerConfiguration.getResultPath() +  "/" + uuid.toString());

        return builder.build();
    }
}
