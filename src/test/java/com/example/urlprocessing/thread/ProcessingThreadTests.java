package com.example.urlprocessing.thread;

import com.example.urlprocessing.domain.UrlResult;
import com.example.urlprocessing.repository.UrlResultRepository;
import io.micrometer.core.instrument.util.IOUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProcessingThreadTests {

    @Mock
    private UrlResultRepository urlResultRepository;

    @Mock
    private JsoupWrapper jsoupWrapper;

    @Captor
    private ArgumentCaptor<UrlResult> urlResultArgumentCaptor;

    @Test
    public void run_whenNoBody_thenThrowsException() throws IOException {
        final UUID uuid = UUID.fromString("5381288a-a45b-47cb-98c3-c1a75c02b850");

        final UrlResult urlResult = new UrlResult();
        urlResult.setId(1L);
        urlResult.setUrl("http://www.example.com");

        when(urlResultRepository.getUrlResultByUuid(uuid)).thenReturn(urlResult);
        when(jsoupWrapper.getDocumentBody("http://www.example.com")).thenReturn(null);

        final ProcessingThread processingThread = new ProcessingThread(uuid, urlResultRepository, jsoupWrapper);
        processingThread.run();

        verify(urlResultRepository).save(urlResultArgumentCaptor.capture());
        assertThat(urlResultArgumentCaptor.getValue().getError()).isEqualTo("No document body");
    }

    @Test
    public void run_whenNoParagraphs_thenUpdateObjectWithCountZero() throws IOException {
        final UUID uuid = UUID.fromString("5381288a-a45b-47cb-98c3-c1a75c02b850");

        final UrlResult urlResult = new UrlResult();
        urlResult.setId(1L);
        urlResult.setUrl("http://www.example.com");

        String content = getFileAsString("NoParagraphs.txt");

        when(urlResultRepository.getUrlResultByUuid(uuid)).thenReturn(urlResult);
        when(jsoupWrapper.getDocumentBody("http://www.example.com")).thenReturn(content);

        final ProcessingThread processingThread = new ProcessingThread(uuid, urlResultRepository, jsoupWrapper);
        processingThread.run();

        verify(urlResultRepository).save(urlResultArgumentCaptor.capture());
        assertThat(urlResultArgumentCaptor.getValue().getError()).isNull();
        assertThat(urlResultArgumentCaptor.getValue().getNrOfParagraphs()).isEqualTo(0);
    }

    @Test
    public void run_whenParagraphsFound_thenUpdateObjectWithCount() throws IOException {
        final UUID uuid = UUID.fromString("5381288a-a45b-47cb-98c3-c1a75c02b850");

        final UrlResult urlResult = new UrlResult();
        urlResult.setId(1L);
        urlResult.setUrl("http://www.example.com");

        String content = getFileAsString("ThreeParagraphs.txt");

        when(urlResultRepository.getUrlResultByUuid(uuid)).thenReturn(urlResult);
        when(jsoupWrapper.getDocumentBody("http://www.example.com")).thenReturn(content);

        final ProcessingThread processingThread = new ProcessingThread(uuid, urlResultRepository, jsoupWrapper);
        processingThread.run();

        verify(urlResultRepository).save(urlResultArgumentCaptor.capture());
        assertThat(urlResultArgumentCaptor.getValue().getError()).isNull();
        assertThat(urlResultArgumentCaptor.getValue().getNrOfParagraphs()).isEqualTo(3);
    }

    private String getFileAsString(final String path) {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream(path);
        return IOUtils.toString(in, StandardCharsets.UTF_8);
    }
}
