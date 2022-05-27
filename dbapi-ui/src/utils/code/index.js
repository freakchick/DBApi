
/**
 * address 请求地址 "127.0.0.1:8520/api"
 * detail 完整示例
 * {
 *   "name": "dbapi",
 *   "note": null,
 *   "path": "test",
 *   "params": [
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
 *   "jsonParam": null,
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

import { LANGUAGE } from '@/utils'
import { generateShellCode } from './shell'

function getRequestUrl(address, detail) {
  // 暂时认为都是http
  return `http://${address}/${detail.path}`
}

function generateParam({ lang, address, detail }) {
  const contentType = detail.contentType
  const requestUrl = getRequestUrl(address, detail)
  return {
    lang,
    address, requestUrl, contentType,
    detail
  }
}

function generateCallExampleCode({ lang, address, detail }) {
  const param = generateParam({ lang, address, detail })
  switch (lang) {
    case LANGUAGE.SHELL:
      return generateShellCode(param)
    default:
      return ''
  }
}


