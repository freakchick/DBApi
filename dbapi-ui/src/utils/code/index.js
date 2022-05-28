
/**
 * address 请求地址 "127.0.0.1:8520/api"
 * detail 完整示例
 * {
 *   "name": "dbapi",
 *   "note": null,
 *   "path": "test",
 *   "params": [
 *     // "contentType": "application/x-www-form-urlencoded"时才有值
 *     {
 *       "name": "id",
 *       "type": "string"
 *     }
 *   ],
 *   "groupId": "djwUzW2E",
 *   "previlege": 1,
 *   "cachePlugin": null,
 *   "cachePluginParams": null,
 *   "alarmPlugin": null,
 *   "alarmPluginParam": null,
 *   // "contentType": "application/json"时才有值
 *   "jsonParam": "{\"id\":\"\"}",
 *   "sqlList": [
 *     {
 *       "sqlText": "-- 请输入sql，一个标签只能输入一条sql\\nselect * from user\\n\\n<where>\\nid = #{id}\\n</where>",
 *       "transformPlugin": null,
 *       "transformPluginParams": null
 *     }
 *   ],
 *   "contentType": "application/x-www-form-urlencoded",
 *   "openTrans": 0
 * }
 */

import { LANGUAGE, CONTENT_TYPE, PREVILEGE } from '@/constant'
import { generateShellCallExampleCode } from './shell'
import { generatePythonCallExampleCode } from './python'
import { generateGoCallExampleCode } from './go'
import { generateJavaScriptCallExampleCode } from './javascript'
import { generateJavaCallExampleCode } from './java'

function getRequestUrl(address, detail) {
  // 暂时认为都是http
  return `http://${address}/${detail.path}`
}

function generateParam({ lang, address, detail }) {
  const contentType = detail.contentType
  const params = detail.params
  const requestUrl = getRequestUrl(address, detail)
  const previlege = detail.previlege
  const isPrevilegePrivate = previlege === PREVILEGE.PRIVATE
  const isPrevilegePublic = previlege === PREVILEGE.PUBLIC
  const isContentTypeJson = contentType === CONTENT_TYPE.JSON
  const isContentTypeFormUrlEncoded = contentType === CONTENT_TYPE.FORM_URLENCODED
  // 暂时不知道token,仅作占位用
  const token = ''
  return {
    lang,
    address, requestUrl,
    isContentTypeJson, isContentTypeFormUrlEncoded, contentType,
    isPrevilegePrivate, isPrevilegePublic, token,
    params,
    detail
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


