const DATA_TYPE = {
  STRING: 'string',
  BIGINT: 'bigint',
  DOUBLE: 'double',
  DATE: 'date',
  ARRAY_STRING: 'Array<string>',
  ARRAY_BIGINT: 'Array<bigint>',
  ARRAY_DOUBLE: 'Array<double>',
  ARRAY_DATE: 'Array<date>',
}

const EXECUTOR_TYPE = {
  SQL_EXECUTOR: 1,
  ES_DSL_EXECUTOR: 2,
  HTTP_EXECUTOR: 3
}

const PLUGIN_TYPE = {
  CACHE_PLUGIN: 1,
  ALARM_PLUGIN: 2,
  CONVERSION_PLUGIN: 3
}


const CONTENT_TYPE = {
  FORM_URLENCODED: "application/x-www-form-urlencoded",
  JSON: "application/json"
}

const PRIVILEGE = {
  PRIVATE: 0,
  PUBLIC: 1
}

export {
  DATA_TYPE,
  CONTENT_TYPE,
  PRIVILEGE,
  EXECUTOR_TYPE,
  PLUGIN_TYPE
}