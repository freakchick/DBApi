DROP TABLE IF EXISTS `access_log`;

CREATE TABLE `access_log`
(
    `id`        varchar(50) NOT NULL,
    `url`       varchar(255)  DEFAULT NULL,
    `status`    int(11) DEFAULT NULL,
    `duration`  bigint(20) DEFAULT NULL,
    `timestamp` bigint(20) DEFAULT NULL,
    `ip`        varchar(50)   DEFAULT NULL,
    `app_id`    varchar(50)   DEFAULT NULL,
    `api_id`    varchar(50)   DEFAULT NULL,
    `error`     varchar(1024) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;