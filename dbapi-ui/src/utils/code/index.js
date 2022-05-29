
/**
 * address 请求地址 "127.0.0.1:8520/api"
 * detail 完整示例
 * {
 *   "path": "test",
 *   "params": [
 *     // "contentType": "application/x-www-form-urlencoded"时才有值
 *     {
 *       "name": "id",
 *       "type": "string"
 *     }
 *   ],
 *   "previlege": 1,
 *   // "contentType": "application/json"时才有值
 *   "jsonParam": "{\"id\":\"\"}",
 *   ],
 *   "contentType": "application/x-www-form-urlencoded",
 * }
 */

import { LANGUAGE, CONTENT_TYPE, PREVILEGE, DATA_TYPE } from '@/constant'
import { generateShellCallExampleCode } from './shell'
import { generatePythonCallExampleCode } from './python'
import { generateGoCallExampleCode } from './go'
import { generateJavaScriptCallExampleCode } from './javascript'
import { generateJavaCallExampleCode } from './java'

function getEffectValue(type, value, expectValue) {
  switch (type) {
    case DATA_TYPE.STRING:
      return typeof value === 'string' ? value : expectValue || ""
    case DATA_TYPE.BIGINT:
      return !isNaN(value) ? value : expectValue || 0
    case DATA_TYPE.DOUBLE:
      return !isNaN(value) ? value : expectValue || 1.0
    case DATA_TYPE.DATE:
      return typeof value === 'string' ? value : expectValue || "1970-01-01 00:00:00"
    case DATA_TYPE.ARRAY_STRING:
      return typeof value === 'string' ? value : expectValue || ""
    case DATA_TYPE.ARRAY_BIGINT:
      return !isNaN(value) ? value : expectValue || 0
    case DATA_TYPE.ARRAY_DOUBLE:
      return !isNaN(value) ? value : expectValue || 1.0
    case DATA_TYPE.ARRAY_DATE:
      return typeof value === 'string' && value.length > 0 ? value : expectValue || "1970-01-01 00:00:00"
    default:
      return ""
  }
}

function generateParam({ lang, address, detail }) {
  const contentType = detail.contentType
  const params = detail.params
  const requestUrl = address
  const previlege = detail.previlege
  const isPrevilegePrivate = previlege === PREVILEGE.PRIVATE
  const isPrevilegePublic = previlege === PREVILEGE.PUBLIC
  const isContentTypeJson = contentType === CONTENT_TYPE.JSON
  const isContentTypeFormUrlEncoded = contentType === CONTENT_TYPE.FORM_URLENCODED
  // 暂时不知道token,仅作占位用
  const token = detail.token || ''
  return {
    lang,
    address, requestUrl,
    isContentTypeJson, isContentTypeFormUrlEncoded, contentType,
    isPrevilegePrivate, isPrevilegePublic, token,
    params,
    detail,
    getEffectValue
  }
}

export function generateCallExampleCode({ lang, address, detail }) {
  const param = generateParam({ lang, address, detail })
  switch (lang) {
    case LANGUAGE.SHELL:
      return generateShellCallExampleCode(param)
    case LANGUAGE.PYTHON:
      return generatePythonCallExampleCode(param)
    case LANGUAGE.GO:
      return generateGoCallExampleCode(param)
    case LANGUAGE.JAVASCRIPT:
      return generateJavaScriptCallExampleCode(param)
    case LANGUAGE.JAVA:
      return generateJavaCallExampleCode(param)
    default:
      return ''
  }
}


