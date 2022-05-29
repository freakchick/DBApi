/**
// npm install axios
// npm install qs
const axios = require('axios');
var qs = require('qs');

axios({
  method: 'post',
  url: 'http://127.0.0.1:8520/api/test',
  
  // application/x-www-form-urlencoded
  headers: {
    'Content-Type': 'application/x-www-form-urlencoded',
    'Authorization': ''
  },
  data: qs.stringify({
    id: 1
  }),

  // application/json
  headers: {
    'Content-Type': 'application/json',
    'Authorization': ''
  },
  data: {
    id: 1
  }

}).then(response => {
  console.log(response.data);
}).catch(error => {
  console.log(error);
});
 */
import { DATA_TYPE, CONTENT_TYPE } from "@/constant";

export function generateJavaScriptCallExampleCode({
  lang,
  address, requestUrl,
  isContentTypeJson, isContentTypeFormUrlEncoded, contentType,
  isPrevilegePrivate, isPrevilegePublic, token,
  params,
  detail,
  getEffectValue
}) {
  const authorizationHeader = `'Authorization': '${token}'`

  let dataDesStr = ''
  if (isContentTypeJson) {
    dataDesStr = detail.jsonParam
  } else if (isContentTypeFormUrlEncoded) {
    const param = getParam(params, getEffectValue)
    dataDesStr = `qs.stringify({
  ${param.join(',\n  ')}
}, { indices: false })`
  }


  return `// npm install axios
// npm install qs
const axios = require('axios');
const qs = require('qs');
const data = ${dataDesStr}
axios({
  method: 'post',
  url: '${requestUrl}',
  headers: {
    'Content-Type': '${contentType}'${isPrevilegePrivate ? ',' : ''}
${isPrevilegePrivate ? "    " + authorizationHeader + "\n" : ''}  },
  data: data,
}).then(response => {
  console.log(response.data);
}).catch(error => {
  console.log(error);
});`
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