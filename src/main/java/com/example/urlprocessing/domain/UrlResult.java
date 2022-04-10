package com.example.urlprocessing.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "url_result")
@NoArgsConstructor
public class UrlResult {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "url")
    private String url;

    @Column(name = "uuid")
    private UUID uuid;

    @Column(name = "nr_of_paragraphs")
    private Integer nrOfParagraphs;

    @Column(name = "error")
    private String error;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "finished_at")
    private LocalDateTime finishedAt;

}
