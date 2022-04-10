package com.example.urlprocessing.web.rest.controller.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class Utils {

    private final ObjectMapper objectMapper;

    public <T> T parse(final ResultActions result, final TypeReference<T> typeReference) {
        return parse(result.andReturn().getResponse().getContentAsByteArray(), typeReference);
    }

    private <T> T parse(byte[] bytes, final TypeReference<T> typeReference) {
        try {
            return objectMapper.readValue(
                    new String(Objects.requireNonNull(bytes), StandardCharsets.UTF_8),
                    typeReference
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
