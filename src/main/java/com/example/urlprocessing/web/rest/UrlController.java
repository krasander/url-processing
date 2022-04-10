package com.example.urlprocessing.web.rest;

import com.example.urlprocessing.domain.UrlResult;
import com.example.urlprocessing.service.UrlService;
import com.example.urlprocessing.web.rest.dto.ProcessResponseDto;
import com.example.urlprocessing.web.rest.dto.ResultResponseDto;
import com.example.urlprocessing.web.rest.dto.mapper.ProcessResponseMapper;
import com.example.urlprocessing.web.rest.dto.mapper.ResultResponseMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("${controller.request-mapping}")
@RequiredArgsConstructor
@Slf4j
public class UrlController {

    private final UrlService urlService;

    private final ProcessResponseMapper processResponseMapper;

    private final ResultResponseMapper resultResponseMapper;

    @PostMapping(
            value = "${controller.process-path}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ProcessResponseDto processUrl(
            @RequestParam(value = "url") final String url
    ) {
        return processResponseMapper.toDto(urlService.processUrl(url));
    }

    @GetMapping(
            value = "${controller.result-path}/{uuid}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResultResponseDto> getResult(
            @PathVariable(value = "uuid") final UUID uuid
    ) {
        final UrlResult result = urlService.getResult(uuid);

        if (result != null) {
            if (result.getFinishedAt() != null) {
                return ResponseEntity.ok(resultResponseMapper.toDto(result)); // Processing completed
            }
            return ResponseEntity.accepted().build(); // Processing not yet completed
        }

        return ResponseEntity.notFound().build();
    }
}
