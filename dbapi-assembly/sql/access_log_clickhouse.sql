CREATE TABLE access_log
(
    `id`        String,
    `url`       String,
    `status`    Int32,
    `duration`  Int64,
    `timestamp` Int64,
    `ip`        String,
    `client_id`    String,
    `api_id`    String,
    `error`     String
) ENGINE = MergeTree
ORDER BY timestamp
SETTINGS index_granularity = 8192