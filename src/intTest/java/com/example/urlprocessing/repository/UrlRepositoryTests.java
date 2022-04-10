package com.example.urlprocessing.repository;

import com.example.urlprocessing.domain.UrlResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("repository-test")
public class UrlRepositoryTests {

    @Autowired
    private UrlResultRepository urlResultRepository;

    @Test
    public void getAllByFinishedAtIsNull_whenNone_thenReturnEmptyList() {
        final List<UrlResult> urlResults = urlResultRepository.getAllByFinishedAtIsNullOrderByCreatedAtAsc();
        assertThat(urlResults).isEmpty();
    }

    @Test
    public void getAllByFinishedAtIsNull_whenExists_thenReturnResultsOrderedByCreatedAt() {
        final LocalDateTime now = LocalDateTime.now();

        final UrlResult firstUnfinishedResult = new UrlResult();
        firstUnfinishedResult.setId(1L);
        firstUnfinishedResult.setCreatedAt(now.plusSeconds(2));
        urlResultRepository.save(firstUnfinishedResult);

        final UrlResult secondUnfinishedResult = new UrlResult();
        secondUnfinishedResult.setCreatedAt(now.minusSeconds(1));
        secondUnfinishedResult.setId(2L);
        urlResultRepository.save(secondUnfinishedResult);

        final UrlResult finishedResult = new UrlResult();
        finishedResult.setId(3L);
        finishedResult.setNrOfParagraphs(1);
        finishedResult.setFinishedAt(now);
        urlResultRepository.save(finishedResult);

        final List<UrlResult> urlResults = urlResultRepository.getAllByFinishedAtIsNullOrderByCreatedAtAsc();

        assertThat(urlResults).hasSize(2);
        assertThat(urlResults).containsExactly(secondUnfinishedResult, firstUnfinishedResult);
    }

    @Test
    public void getUrlResultByUuid_whenNoMatch_thenReturnNull() {
        final UUID uuid = UUID.fromString("5381288a-a45b-47cb-98c3-c1a75c02b850");

        final UrlResult result = new UrlResult();
        result.setId(1L);
        result.setUuid(uuid);
        result.setNrOfParagraphs(2);
        urlResultRepository.save(result);

        final UrlResult urlResult = urlResultRepository.getUrlResultByUuid(UUID.fromString("dd9e4e6e-469e-4329-a5df-d4c6dcf77588"));

        assertThat(urlResult).isNull();
    }

    @Test
    public void getUrlResultByUuid_whenMatch_thenReturnIt() {
        final UUID firstUuid = UUID.fromString("5381288a-a45b-47cb-98c3-c1a75c02b850");

        final UrlResult firstResult = new UrlResult();
        firstResult.setId(1L);
        firstResult.setUuid(firstUuid);
        firstResult.setNrOfParagraphs(3);
        urlResultRepository.save(firstResult);

        final UUID secondUuid = UUID.fromString("dd9e4e6e-469e-4329-a5df-d4c6dcf77588");

        final UrlResult secondResult = new UrlResult();
        secondResult.setId(2L);
        secondResult.setUuid(secondUuid);
        secondResult.setNrOfParagraphs(4);
        urlResultRepository.save(secondResult);

        final UrlResult urlResult = urlResultRepository.getUrlResultByUuid(firstUuid);

        assertThat(urlResult).isEqualTo(firstResult);
    }
}
