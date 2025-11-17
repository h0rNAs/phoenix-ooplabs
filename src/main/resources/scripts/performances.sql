CREATE TABLE IF NOT EXISTS query_performance
(
    query VARCHAR NOT NULL,
    manual_duration BIGINT,
    framework_duration BIGINT
);

CREATE TABLE IF NOT EXISTS sorting_performance
(
    query VARCHAR NOT NULL,
    manual_duration BIGINT,
    framework_duration BIGINT
)