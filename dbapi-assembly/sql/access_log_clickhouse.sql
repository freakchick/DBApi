CREATE TABLE access_log
(
    `id`        String,
    `url`       String,
    `status`    Int32,
    `duration`  Int64,
    `timestamp` Int64,
    `ip`        String,
    `app_id`    String,
    `api_id`    String,
    `error`     String
) ENGINE = MergeTree
ORDER BY id
SETTINGS index_granularity = 8192