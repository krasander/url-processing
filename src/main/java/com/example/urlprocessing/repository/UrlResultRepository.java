package com.example.urlprocessing.repository;

import com.example.urlprocessing.domain.UrlResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Repository
public interface UrlResultRepository extends JpaRepository<UrlResult, Long> {

    @Modifying
    @Transactional
    @Query(
            value = "insert into url_result (url, uuid) " +
                    "select :url, :uuid",
            nativeQuery = true)
    void addUrlToProcess(@Param("url") String url, @Param("uuid") UUID uuid);

    UrlResult getUrlResultByUuid(@Param("uuid") UUID uuid);

    List<UrlResult> getAllByFinishedAtIsNullOrderByCreatedAtAsc();
}
