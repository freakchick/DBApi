drop table if exists api_config;
CREATE TABLE `api_config`
(
    `id`                      int(11) NOT NULL AUTO_INCREMENT,
    `path`                    varchar(255) DEFAULT NULL,
    `name`                    varchar(255) DEFAULT NULL,
    `note`                    varchar(255) DEFAULT NULL,
    `sql`                     text,
    `params`                  text,
    `status`                  int(11)      DEFAULT NULL,
    `datasource_id`           int(11)      DEFAULT NULL,
    `real_sql`                text,
    `is_select`               int(11)      DEFAULT NULL,
    `previlege`               int(11)      DEFAULT NULL,
    `group_id`                int(11)      DEFAULT NULL,
    `cache_plugin`            varchar(255) DEFAULT NULL,
    `cache_plugin_params`     varchar(255) DEFAULT NULL,
    `transform_plugin`        varchar(255) DEFAULT NULL,
    `transform_plugin_params` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `path` (`path`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


drop table if exists `datasource`;
CREATE TABLE `datasource`
(
    `id`       int(11) NOT NULL AUTO_INCREMENT,
    `name`     varchar(255) DEFAULT NULL,
    `note`     varchar(255) DEFAULT NULL,
    `type`     varchar(255) DEFAULT NULL,
    `url`      varchar(255) DEFAULT NULL,
    `username` varchar(255) DEFAULT NULL,
    `password` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

drop table if exists `api_auth`;
CREATE TABLE `api_auth`
(
    `id`       int(11) NOT NULL AUTO_INCREMENT,
    `token_id` int(11) DEFAULT NULL,
    `group_id` int(11) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

drop table if exists `api_group`;
CREATE TABLE `api_group`
(
    `id`   int(11) NOT NULL AUTO_INCREMENT,
    `name` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `name` (`name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

drop table if exists `token`;
CREATE TABLE `token`
(
    `id`          int(11) NOT NULL AUTO_INCREMENT,
    `token`       varchar(255) DEFAULT NULL,
    `note`        varchar(255) DEFAULT NULL,
    `expire`      bigint(20)   DEFAULT NULL,
    `create_time` bigint(20)   DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


drop table if exists `user`;
CREATE TABLE `user`
(
    `id`       int(11) NOT NULL AUTO_INCREMENT,
    `username` varchar(255) DEFAULT NULL,
    `password` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `username` (`username`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
insert into `user` (username, password)
values ('admin', 'admin');

drop table if exists `firewall`;
CREATE TABLE `firewall`
(
    `status` varchar(255) DEFAULT NULL,
    `mode`   varchar(255) DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
insert into `firewall` (status, mode)
values ('off', 'black');

drop table if exists `ip_rules`;
CREATE TABLE `ip_rules`
(
    `type` varchar(255)   DEFAULT NULL,
    `ip`   varchar(10240) DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
insert into `ip_rules` (type)
VALUES ('white');
insert into `ip_rules` (type)
VALUES ('black');
