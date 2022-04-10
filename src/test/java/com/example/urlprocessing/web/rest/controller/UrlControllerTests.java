package com.example.urlprocessing.web.rest.controller;

import com.example.urlprocessing.config.ControllerConfiguration;
import com.example.urlprocessing.domain.UrlResult;
import com.example.urlprocessing.service.UrlService;
import com.example.urlprocessing.web.rest.UrlController;
import com.example.urlprocessing.web.rest.controller.utils.Utils;
import com.example.urlprocessing.web.rest.dto.ProcessResponseDto;
import com.example.urlprocessing.web.rest.dto.ResultResponseDto;
import com.example.urlprocessing.web.rest.dto.mapper.ProcessResponseMapper;
import com.example.urlprocessing.web.rest.dto.mapper.ResultResponseMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UrlController.class)
@Import({Utils.class, ProcessResponseMapper.class, ResultResponseMapper.class, ControllerConfiguration.class})
public class UrlControllerTests {

    private static final TypeReference<ProcessResponseDto> PROCESS_RESPONSE_DTO_TYPE_REFERENCE =
            new TypeReference<>() {};

    private static final TypeReference<ResultResponseDto> RESULT_RESPONSE_DTO_TYPE_REFERENCE =
            new TypeReference<>() {};

    @MockBean
    private UrlService urlService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Utils utils;

    @Test
    public void processUrl_whenCalled_thenReturnUrl() throws Exception {
        final UUID uuid = UUID.fromString("5381288a-a45b-47cb-98c3-c1a75c02b850");

        final String url = "http://www.example.com";

        when(urlService.processUrl(url)).thenReturn(uuid);

        final ResultActions result = mockMvc.perform(
                post("/api/url/process")
                        .accept(MediaType.APPLICATION_JSON)
                        .queryParam("url", url)
        );

        result.andExpect(status().isOk());

        assertThat(parseProcessResponse(result)).isEqualTo(
                ProcessResponseDto.builder()
                        .url("/api/url/result/" + uuid)
                        .build()
        );

        verify(urlService).processUrl(url);
    }

    @Test
    public void getResult_whenNotFound_thenReturnNotFound() throws Exception {
        final UUID uuid = UUID.fromString("5381288a-a45b-47cb-98c3-c1a75c02b850");

        when(urlService.getResult(uuid)).thenReturn(null);

        final ResultActions result = mockMvc.perform(
                get("/api/url/result/" + uuid)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isNotFound());

        verify(urlService).getResult(uuid);
    }

    @Test
    public void getResult_whenProcessingNotFinished_thenReturnAccepted() throws Exception {
        final UUID uuid = UUID.fromString("5381288a-a45b-47cb-98c3-c1a75c02b850");

        final UrlResult urlResult = new UrlResult();
        urlResult.setId(1L);

        when(urlService.getResult(uuid)).thenReturn(urlResult);

        final ResultActions result = mockMvc.perform(
                get("/api/url/result/" + uuid)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isAccepted());

        verify(urlService).getResult(uuid);
    }

    @Test
    public void getResult_whenProcessingFinished_thenReturnResult() throws Exception {
        final UUID uuid = UUID.fromString("5381288a-a45b-47cb-98c3-c1a75c02b850");

        final LocalDateTime now = LocalDateTime.now();

        final UrlResult urlResult = new UrlResult();
        urlResult.setId(1L);
        urlResult.setUrl("http://www.example.com");
        urlResult.setNrOfParagraphs(25);
        urlResult.setCreatedAt(now.minusSeconds(10));
        urlResult.setStartedAt(now.minusSeconds(5));
        urlResult.setFinishedAt(now);

        when(urlService.getResult(uuid)).thenReturn(urlResult);

        final ResultActions result = mockMvc.perform(
                get("/api/url/result/" + uuid)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk());

        assertThat(parseResultResponse(result)).isEqualTo(ResultResponseDto.builder()
                .url("http://www.example.com")
                .nrOfParagraphs(25)
                .error(null)
                .createdAt(now.minusSeconds(10))
                .startedAt(now.minusSeconds(5))
                .finishedAt(now)
                .build());

        verify(urlService).getResult(uuid);
    }

    private ProcessResponseDto parseProcessResponse(ResultActions response) {
        return utils.parse(response, PROCESS_RESPONSE_DTO_TYPE_REFERENCE);
    }

    private ResultResponseDto parseResultResponse(ResultActions response) {
        return utils.parse(response, RESULT_RESPONSE_DTO_TYPE_REFERENCE);
    }

}
