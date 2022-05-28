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
import { DATA_TYPE, CONTENT_TYPE, PREVILEGE } from "@/constant";

export function generateShellCallExampleCode({
  lang,
  address, requestUrl,
  isContentTypeJson, isContentTypeFormUrlEncoded, contentType,
  isPrevilegePrivate, isPrevilegePublic, token,
  params,
  detail
}) {
  let realRequestStr = ''
  if (isContentTypeFormUrlEncoded) {
    const param = getParam(params)
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

function getParam(params) {
  const param = []
  params.forEach(item => {
    const itemType = item.type
    const itemName = item.name
    switch (itemType) {
      case DATA_TYPE.STRING:
        param.push(`--data-urlencode ${itemName}=""`)
        break
      case DATA_TYPE.BIGINT:
        param.push(`--data-urlencode ${itemName}=0`)
        break
      case DATA_TYPE.DOUBLE:
        param.push(`--data-urlencode ${itemName}=1.0`)
        break
      case DATA_TYPE.DATE:
        param.push(`--data-urlencode ${itemName}="1970-01-01 00:00:00"`)
        break
      case DATA_TYPE.ARRAY_STRING:
        param.push(`--data-urlencode ${itemName}=""`)
        param.push(`--data-urlencode ${itemName}=""`)
        break
      case DATA_TYPE.ARRAY_BIGINT:
        param.push(`--data-urlencode ${itemName}=1`)
        param.push(`--data-urlencode ${itemName}=2`)
        break
      case DATA_TYPE.ARRAY_DOUBLE:
        param.push(`--data-urlencode ${itemName}=1.0`)
        param.push(`--data-urlencode ${itemName}=1.0`)
        break
      case DATA_TYPE.ARRAY_DATE:
        param.push(`--data-urlencode ${itemName}="1970-01-01 00:00:00"`)
        param.push(`--data-urlencode ${itemName}="1970-01-02 00:00:00"`)
        break
      default:
        break
    }
  })
  return param
}