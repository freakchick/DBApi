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

const CONTENT_TYPE = {
  FORM_URLENCODED: "application/x-www-form-urlencoded",
  JSON: "application/json"
}

const PREVILEGE = {
  PRIVATE: 0,
  PUBLIC: 1
}

export {
  DATA_TYPE,
  CONTENT_TYPE,
  PREVILEGE
}