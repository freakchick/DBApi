CREATE TABLE "api_config"
(
    "id"            INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "path"          TEXT,
    "name"          TEXT,
    "note"          TEXT,
    "sql"           TEXT,
    "params"        TEXT,
    "status"        integer,
    "datasource_id" INTEGER,
    "real_sql"      TEXT,
    "is_select"     integer,
    "previlege"     integer,
    "group_id"      integer,
    UNIQUE ("path") ON CONFLICT ABORT
);

CREATE TABLE "datasource"
(
    "id"       INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "name"     TEXT,
    "note"     TEXT,
    "type"     TEXT,
    "url"      TEXT,
    "username" TEXT,
    "password" TEXT
);

CREATE TABLE "api_auth"
(
    "id"       INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "token_id" INTEGER,
    "group_id" INTEGER
);

CREATE TABLE "api_group"
(
    "id"   INTEGER NOT NULL,
    "name" TEXT    NOT NULL,
    PRIMARY KEY ("id", "name"),
    UNIQUE ("name") ON CONFLICT ABORT
);

CREATE TABLE "token"
(
    "id"          INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "token"       TEXT,
    "expire"      integer,
    "create_time" integer,
    "note"        TEXT
);

CREATE TABLE "user"
(
    "id"       INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "username" TEXT,
    "password" TEXT,
    UNIQUE ("username") ON CONFLICT ABORT
);

insert into user (username, password)
values ('admin', 'admin');