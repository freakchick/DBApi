import { DATA_TYPE, CONTENT_TYPE } from "@/constant";

export function generatePythonCallExampleCode({
  lang,
  address, requestUrl,
  isContentTypeJson, isContentTypeFormUrlEncoded, contentType,
  isPrevilegePrivate, isPrevilegePublic, token,
  params,
  detail,
  getEffectValue
}) {
  const authorizationHeader = `  'Authorization': '${token}'`
  let dataDesStr = ''
  if (isContentTypeJson) {
    dataDesStr = `jsonParam = ${detail.jsonParam}
data = json.dumps(jsonParam)`
  } else if (isContentTypeFormUrlEncoded) {
    const param = getParam(params, getEffectValue)
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

function getParam(params, getEffectValue) {
  const param = []
  params.forEach(item => {
    const itemType = item.type
    const itemName = `"${item.name}"`
    // {"name":"id","type":"string","value":"1"}
    // {"name":"ids","type":"Array<string>","values":[{"va":"2"},{"va":"3"}]}
    // {"name":"date","type":"date","value":"1970-01-01 00:00:00"}
    // {"name":"dates","type":"Array<date>","values":[{"va":"1970-01-01 00:00:00"},{"va":"1970-01-01 00:00:00"}]}
    const value = getEffectValue(itemType, item.value)
    const values = Array.isArray(item.values) ? item.values : []
    let elements = ''
    switch (itemType) {
      case DATA_TYPE.STRING:
        param.push(`${itemName}: "${value}"`)
        break
      case DATA_TYPE.BIGINT:
        param.push(`${itemName}: ${value}`)
        break
      case DATA_TYPE.DOUBLE:
        param.push(`${itemName}: ${value}`)
        break
      case DATA_TYPE.DATE:
        param.push(`${itemName}: "${value}"`)
        break
      case DATA_TYPE.ARRAY_STRING:
        if (values.length > 0) {
          elements = values.map(el => getEffectValue(itemType, el.va)).join("\", \"")
        }
        param.push(`${itemName}: ["${elements}"]`)
        break
      case DATA_TYPE.ARRAY_BIGINT:
        elements = '1'
        if (values.length > 0) {
          elements = values.map(el => getEffectValue(itemType, el.va)).join(",")
        }
        param.push(`${itemName}: [${elements}]`)
        break
      case DATA_TYPE.ARRAY_DOUBLE:
        elements = '1.0'
        if (values.length > 0) {
          elements = values.map(el => getEffectValue(itemType, el.va)).join(",")
        }
        param.push(`${itemName}: [${elements}]`)
        break
      case DATA_TYPE.ARRAY_DATE:
        elements = '1970-01-01 00:00:00'
        if (values.length > 0) {
          elements = values.map(el => getEffectValue(itemType, el.va)).join("\", \"")
        }
        param.push(`${itemName}: ["${elements}"]`)
        break
      default:
        break
    }
  })
  return param
}