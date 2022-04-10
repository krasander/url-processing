package com.example.urlprocessing.service;

import com.example.urlprocessing.domain.UrlResult;
import com.example.urlprocessing.repository.UrlResultRepository;
import com.example.urlprocessing.thread.JsoupWrapper;
import com.example.urlprocessing.thread.MyExecutor;
import com.example.urlprocessing.thread.ProcessingThread;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class UrlService {

    private final UrlResultRepository urlResultRepository;

    private final MyExecutor myExecutor;

    private final JsoupWrapper jsoupWrapper;

    @PostConstruct
    public void processUnfinishedUrls() {
        final List<UrlResult> results = urlResultRepository.getAllByFinishedAtIsNullOrderByCreatedAtAsc();
        for (UrlResult result : results) {
            myExecutor.execute(new ProcessingThread(result.getUuid(), urlResultRepository, jsoupWrapper));
        }
    }

    public UUID processUrl(final String url) {
        final UUID uuid = UUID.randomUUID();
        urlResultRepository.addUrlToProcess(url, uuid);
        myExecutor.execute(new ProcessingThread(uuid, urlResultRepository, jsoupWrapper));
        return uuid;
    }

    public UrlResult getResult(final UUID uuid) {
        return urlResultRepository.getUrlResultByUuid(uuid);
    }
}
