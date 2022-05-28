import { DATA_TYPE, CONTENT_TYPE } from "@/constant";

export function generatePythonCallExampleCode({
  lang,
  address, requestUrl,
  isContentTypeJson, isContentTypeFormUrlEncoded, contentType,
  isPrevilegePrivate, isPrevilegePublic, token,
  params,
  detail
}) {
  const authorizationHeader = `  'Authorization': '${token}'`
  let dataDesStr = ''
  if (isContentTypeJson) {
    dataDesStr = `jsonParam = ${detail.jsonParam}
data = json.dumps(jsonParam)`
  } else if (isContentTypeFormUrlEncoded) {
    const param = getParam(params)
    dataDesStr = `formData = {
  ${param.join(',\n  ')}
}
data = parse.urlencode(formData, True)`
  }

  return `import requests${isContentTypeJson ? '\nimport json' : ''}${isContentTypeFormUrlEncoded ? '\nfrom urllib import parse' : ''}

requestUrl = '${requestUrl}'
headers = {
  'Content-Type': '${contentType}'${isPrevilegePrivate ? ',' : ''}
${isPrevilegePrivate ? authorizationHeader + '\n' : ''}}
${dataDesStr}

response = requests.post(requestUrl, headers = headers, data = data)
print(response.status_code)
print(response.text)`
}

function getParam(params) {
  const param = []
  params.forEach(item => {
    const itemType = item.type
    const itemName = `"${item.name}"`
    switch (itemType) {
      case DATA_TYPE.STRING:
        param.push(`${itemName}: ""`)
        break
      case DATA_TYPE.BIGINT:
        param.push(`${itemName}: 0`)
        break
      case DATA_TYPE.DOUBLE:
        param.push(`${itemName}: 1.0`)
        break
      case DATA_TYPE.DATE:
        param.push(`${itemName}: "1970-01-01 00:00:00"`)
        break
      case DATA_TYPE.ARRAY_STRING:
        param.push(`${itemName}: [""]`)
        break
      case DATA_TYPE.ARRAY_BIGINT:
        param.push(`${itemName}: [1]`)
        break
      case DATA_TYPE.ARRAY_DOUBLE:
        param.push(`${itemName}: [1.0]`)
        break
      case DATA_TYPE.ARRAY_DATE:
        param.push(`${itemName}: ["1970-01-01 00:00:00"]`)
        break
      default:
        break
    }
  })
  return param
}