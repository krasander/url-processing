package com.example.urlprocessing.service;

import com.example.urlprocessing.domain.UrlResult;
import com.example.urlprocessing.repository.UrlResultRepository;
import com.example.urlprocessing.thread.MyExecutor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UrlServiceTests {

    @Mock
    private UrlResultRepository urlResultRepository;

    @Mock
    private MyExecutor myExecutor;

    @InjectMocks
    private UrlService urlService;

    @Test
    public void processUrl_whenCalled_thenQueuesThread() {
        final UUID uuid = urlService.processUrl("http://www.example.com");
        verify(urlResultRepository).addUrlToProcess("http://www.example.com", uuid);
        verify(myExecutor).execute(any());
    }

    @Test
    public void getResult_whenCalled_thenReturnsResult() {
        final UUID uuid = UUID.fromString("5381288a-a45b-47cb-98c3-c1a75c02b850");

        final UrlResult urlResult = new UrlResult();
        urlResult.setId(1L);
        urlResult.setUuid(uuid);

        when(urlResultRepository.getUrlResultByUuid(uuid)).thenReturn(urlResult);

        final UrlResult result = urlService.getResult(uuid);

        assertThat(result).isEqualTo(urlResult);
        verify(urlResultRepository).getUrlResultByUuid(uuid);
    }

    @Test
    public void processUnfinishedUrls_whenUnfinishedUrls_thenQueueThem() {
        UrlResult firstResult = new UrlResult();
        firstResult.setId(1L);

        UrlResult secondResult = new UrlResult();
        secondResult.setId(2L);

        final List<UrlResult> urlResults = Arrays.asList(firstResult, secondResult);

        when(urlResultRepository.getAllByFinishedAtIsNullOrderByCreatedAtAsc()).thenReturn(urlResults);

        urlService.processUnfinishedUrls();

        verify(urlResultRepository).getAllByFinishedAtIsNullOrderByCreatedAtAsc();
        verify(myExecutor, times(2)).execute(any());
    }
}
