create table url_result
(
    id bigserial PRIMARY KEY,
    url varchar(255),
    uuid uuid default null,
    nr_of_paragraphs integer default null,
    error varchar(255) default null,
    created_at timestamp default now(),
    started_at timestamp default null,
    finished_at timestamp default null
)
