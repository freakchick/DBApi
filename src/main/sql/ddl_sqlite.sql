CREATE TABLE "api_config" (
  "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
  "path" TEXT,
  "name" TEXT,
  "note" TEXT,
  "sql" TEXT,
  "params" TEXT,
  "status" integer,
  "datasource_id" INTEGER,
  "real_sql" TEXT,
  "is_select" integer
);

CREATE TABLE "datasource" (
  "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
  "name" TEXT,
  "note" TEXT,
  "type" TEXT,
  "url" TEXT,
  "username" TEXT,
  "password" TEXT
);