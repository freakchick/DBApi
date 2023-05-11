DROP TABLE IF EXISTS `client_auth`;

CREATE TABLE `client_auth`
(
    `id`        int(11) NOT NULL AUTO_INCREMENT,
    `client_id` varchar(20) DEFAULT NULL,
    `group_id`  varchar(20) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `api_config`;

CREATE TABLE `api_config`
(
    `id`           varchar(20) NOT NULL,
    `name`         varchar(255) DEFAULT NULL,
    `note`         varchar(255) DEFAULT NULL,
    `path`         varchar(255) DEFAULT NULL,
    `params`       text,
    `json_param`   text,
    `status`       int(11) DEFAULT NULL COMMENT '0-offline;1-online',
    `access`       int(11) DEFAULT NULL COMMENT '0-private;1-public',
    `group_id`     varchar(20)  DEFAULT NULL,
    `content_type` varchar(50)  DEFAULT NULL,
    `task`         text,
    `create_time`  varchar(20)  DEFAULT NULL,
    `update_time`  varchar(20)  DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `path` (`path`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `api_plugin_config`;
CREATE TABLE `api_plugin_config`
(
    `api_id`       varchar(20) NOT NULL,
    `plugin_type`  int(11) DEFAULT NULL,
    `plugin_name`  varchar(255) DEFAULT NULL,
    `plugin_param` text
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `api_group`;

CREATE TABLE `api_group`
(
    `id`   varchar(255) NOT NULL,
    `name` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


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


DROP TABLE IF EXISTS `user`;

CREATE TABLE `user`
(
    `id`       int(11) NOT NULL AUTO_INCREMENT,
    `username` varchar(255) DEFAULT NULL,
    `password` varchar(255) DEFAULT NULL,
    `type`     int(11) DEFAULT NULL,
    `email`    varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `client`;

CREATE TABLE `client`
(
    `id`              varchar(255) NOT NULL DEFAULT '',
    `name`            varchar(255)          DEFAULT NULL,
    `note`            varchar(1024)         DEFAULT NULL,
    `secret`          varchar(255)          DEFAULT NULL,
    `expire_desc`     varchar(255)          DEFAULT NULL,
    `expire_duration` varchar(255)          DEFAULT NULL,
    `token`           varchar(255)          DEFAULT NULL,
    `expire_at`       bigint(32) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into `firewall`(`status`, `mode`)
values ('off', 'black');

insert into `ip_rules`(`type`, `ip`)
values ('white', NULL),
       ('black', NULL);

insert into `user`(`id`, `username`, `password`, `type`)
values (1, 'admin', '21232f297a57a5a743894a0e4a801fc3', 1);