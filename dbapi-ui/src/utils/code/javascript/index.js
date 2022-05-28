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
  detail
}) {
  const authorizationHeader = `'Authorization': '${token}'`

  let dataDesStr = ''
  if (isContentTypeJson) {
    dataDesStr = detail.jsonParam
  } else if (isContentTypeFormUrlEncoded) {
    const param = getParam(params)
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

function getParam(params) {
  const param = []
  params.forEach(item => {
    const itemType = item.type
    const itemName = item.name
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