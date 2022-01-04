DROP TABLE IF EXISTS `api_auth`;

CREATE TABLE `api_auth`
(
    `id`       int(11) NOT NULL AUTO_INCREMENT,
    `token_id` int(11)      DEFAULT NULL,
    `group_id` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


DROP TABLE IF EXISTS `api_config`;

CREATE TABLE `api_config`
(
    `id`                      varchar(255) NOT NULL,
    `path`                    varchar(255) DEFAULT NULL,
    `name`                    varchar(255) DEFAULT NULL,
    `note`                    varchar(255) DEFAULT NULL,
    `params`                  text,
    `status`                  int(11)      DEFAULT NULL,
    `datasource_id`           varchar(255) DEFAULT NULL,
    `previlege`               int(11)      DEFAULT NULL,
    `group_id`                varchar(255) DEFAULT NULL,
    `cache_plugin`            varchar(255) DEFAULT NULL,
    `cache_plugin_params`     varchar(255) DEFAULT NULL,
    `transform_plugin`        varchar(255) DEFAULT NULL,
    `transform_plugin_params` varchar(255) DEFAULT NULL,
    `create_time`             varchar(20)  DEFAULT NULL,
    `update_time`             varchar(20)  DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `path` (`path`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `api_sql`;
CREATE TABLE `api_sql`
(
    `id`                     int(11)     NOT NULL AUTO_INCREMENT,
    `api_id`                 varchar(11) NOT NULL,
    `sql_text`               text        NOT NULL,
    `transform_plugin`       varchar(255) DEFAULT NULL,
    `transform_plugin_params` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `api_group`;

CREATE TABLE `api_group`
(
    `id`   varchar(255) NOT NULL,
    `name` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `name` (`name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


DROP TABLE IF EXISTS `datasource`;

CREATE TABLE `datasource`
(
    `id`          varchar(255) NOT NULL,
    `name`        varchar(255) DEFAULT NULL,
    `note`        varchar(255) DEFAULT NULL,
    `type`        varchar(255) DEFAULT NULL,
    `url`         varchar(255) DEFAULT NULL,
    `username`    varchar(255) DEFAULT NULL,
    `password`    varchar(255) DEFAULT NULL,
    `driver`      varchar(100) DEFAULT NULL,
    `table_sql`   varchar(255) DEFAULT NULL,
    `create_time` varchar(20)  DEFAULT NULL,
    `update_time` varchar(20)  DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


DROP TABLE IF EXISTS `firewall`;

CREATE TABLE `firewall`
(
    `status` varchar(255) DEFAULT NULL,
    `mode`   varchar(255) DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


DROP TABLE IF EXISTS `ip_rules`;

CREATE TABLE `ip_rules`
(
    `type` varchar(255)   DEFAULT NULL,
    `ip`   varchar(10240) DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


DROP TABLE IF EXISTS `token`;

CREATE TABLE `token`
(
    `id`          int(11) NOT NULL AUTO_INCREMENT,
    `token`       varchar(255) DEFAULT NULL,
    `note`        varchar(255) DEFAULT NULL,
    `expire`      bigint(20)   DEFAULT NULL,
    `create_time` bigint(20)   DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;


DROP TABLE IF EXISTS `user`;

CREATE TABLE `user`
(
    `id`       int(11) NOT NULL AUTO_INCREMENT,
    `username` varchar(255) DEFAULT NULL,
    `password` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `username` (`username`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;

insert into `firewall`(`status`, `mode`)
values ('off', 'black');

insert into `ip_rules`(`type`, `ip`)
values ('white', NULL),
       ('black', NULL);

insert into `user`(`id`, `username`, `password`)
values (1, 'admin', 'admin');