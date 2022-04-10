package com.example.urlprocessing.thread;

import com.example.urlprocessing.domain.UrlResult;
import com.example.urlprocessing.repository.UrlResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Slf4j
public class ProcessingThread implements Runnable {

    private final UUID uuid;

    private final UrlResultRepository urlResultRepository;

    private final JsoupWrapper jsoupWrapper;

    @Override
    public void run() {
        long threadId = Thread.currentThread().getId();
        log.info("Thread {} processing uuid {} ", threadId, uuid);
        try {
            final UrlResult urlResult = urlResultRepository.getUrlResultByUuid(uuid);
            try {
                urlResult.setStartedAt(LocalDateTime.now());
                String body = jsoupWrapper.getDocumentBody(urlResult.getUrl());
                if (body == null) {
                    throw new Exception("No document body");
                }

                int paragraphs = findNrOfParagraphs(body);
                log.info("Number of paragraphs at url {}: {}", urlResult.getUrl(), paragraphs);
                urlResult.setNrOfParagraphs(paragraphs);
            } catch (Exception e) {
                log.error("Error processing uuid {}: {}", uuid, e.getMessage());
                urlResult.setError(e.getMessage());
            } finally {
                urlResult.setFinishedAt(LocalDateTime.now());
                urlResultRepository.save(urlResult);
            }
        } catch (Exception e) {
            log.error("Error fetching uuid {} from repository: {}", uuid, e.getMessage());
        }
    }

    private int findNrOfParagraphs(final String body) {
        Matcher matcher = Pattern
                .compile("<p(|\\s+[^>]*)>((?s).*?)<\\/p\\s*>") // This matches all <p> elements
                .matcher(body);

        int paragraphs = 0;
        while (matcher.find()) {
            paragraphs++;
        }

        return paragraphs;
    }
}
