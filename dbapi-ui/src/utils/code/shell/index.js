/**
 * application/json:
 * curl  -H 'Content-Type: application/json' \ 
 *  -X POST \
 *  --data-raw '{"id":1}' \
 *  'http://127.0.0.1:8520/api/test-json'
 * 
 * application/x-www-form-urlencoded:
 * curl  -H 'Content-Type: application/x-www-form-urlencoded' \
 *  -X POST \
 *  --data-urlencode id=1 \
 *  'http://127.0.0.1:8520/api/test'
 */
import { DATA_TYPE, CONTENT_TYPE, PRIVILEGE } from "@/constant";

export function generateShellCallExampleCode({
  lang,
  address, requestUrl,
  isContentTypeJson, isContentTypeFormUrlEncoded, contentType,
  isPrevilegePrivate, isPrevilegePublic, token,
  params,
  detail,
  getEffectValue
}) {
  let realRequestStr = ''
  if (isContentTypeFormUrlEncoded) {
    const param = getParam(params, getEffectValue)
    realRequestStr = `${param.join(' ')}`
  } else if (isContentTypeJson) {
    const requestStr = detail.jsonParam.replace(/\n|\r|\t/g, '').replace(/"/g, '\\"')
    realRequestStr = `--data-raw '${requestStr}'`
  }
  let previlegeStr = ''
  if (isPrevilegePrivate) {
    previlegeStr = `-H 'Authorization: ${token}'`
  }

  return `curl ${previlegeStr} -H 'Content-Type: ${contentType}' -X POST ${realRequestStr} '${requestUrl}'`
}

function getParam(params, getEffectValue) {
  const param = []
  params.forEach(item => {
    const itemType = item.type
    const itemName = item.name
    // {"name":"id","type":"string","value":"1"}
    // {"name":"ids","type":"Array<string>","values":[{"va":"2"},{"va":"3"}]}
    // {"name":"date","type":"date","value":"1970-01-01 00:00:00"}
    // {"name":"dates","type":"Array<date>","values":[{"va":"1970-01-01 00:00:00"},{"va":"1970-01-01 00:00:00"}]}
    const value = getEffectValue(itemType, item.value)
    const values = Array.isArray(item.values) ? item.values : []
    switch (itemType) {
      case DATA_TYPE.STRING:
        param.push(`--data-urlencode ${itemName}="${value}"`)
        break
      case DATA_TYPE.BIGINT:
        param.push(`--data-urlencode ${itemName}=${value}`)
        break
      case DATA_TYPE.DOUBLE:
        param.push(`--data-urlencode ${itemName}=${value}`)
        break
      case DATA_TYPE.DATE:
        param.push(`--data-urlencode ${itemName}="${value}"`)
        break
      case DATA_TYPE.ARRAY_STRING:
        if (values.length > 0) {
          values.forEach(el => {
            const effectValue = getEffectValue(itemType, el.va)
            param.push(`--data-urlencode ${itemName}="${effectValue}"`)
          })
        } else {
          param.push(`--data-urlencode ${itemName}=""`)
          param.push(`--data-urlencode ${itemName}=""`)
        }
        break
      case DATA_TYPE.ARRAY_BIGINT:
        if (values.length > 0) {
          values.forEach(el => {
            const effectValue = getEffectValue(itemType, el.va)
            param.push(`--data-urlencode ${itemName}=${effectValue}`)
          })
        } else {
          param.push(`--data-urlencode ${itemName}=1`)
          param.push(`--data-urlencode ${itemName}=2`)
        }
        break
      case DATA_TYPE.ARRAY_DOUBLE:
        if (values.length > 0) {
          values.forEach(el => {
            const effectValue = getEffectValue(itemType, el.va)
            param.push(`--data-urlencode ${itemName}=${effectValue}`)
          })
        } else {
          param.push(`--data-urlencode ${itemName}=1.0`)
          param.push(`--data-urlencode ${itemName}=1.0`)
        }
        break
      case DATA_TYPE.ARRAY_DATE:
        if (values.length > 0) {
          values.forEach(el => {
            const effectValue = getEffectValue(itemType, el.va)
            param.push(`--data-urlencode ${itemName}="${effectValue}"`)
          })
        } else {
          param.push(`--data-urlencode ${itemName}="1970-01-01 00:00:00"`)
          param.push(`--data-urlencode ${itemName}="1970-01-02 00:00:00"`)
        }
        break
      default:
        break
    }
  })
  return param
}