import { DATA_TYPE, CONTENT_TYPE } from "@/constant";

export function generateGoCallExampleCode({
  lang,
  address, requestUrl,
  isContentTypeJson, isContentTypeFormUrlEncoded, contentType,
  isPrevilegePrivate, isPrevilegePublic, token,
  params,
  detail,
  getEffectValue
}) {

  const authHeaderStr = `    SetHeader("Authorization", "${token}").\n`

  let bodyStringDesStr = ''
  if (isContentTypeJson) {
    bodyStringDesStr = `\n  bodyString := "${detail.jsonParam.replace(/\n|\r|\t/g, '').replace(/"/g, '\\"')}"\n`
  } else if (isContentTypeFormUrlEncoded) {
    const param = getParam(params, getEffectValue)
    bodyStringDesStr = `data := url.Values{}
  ${param.join("\n  ")}
  bodyString := data.Encode()`
  }

  return `package main
import (
  "fmt"
  // go get -u github.com/imroc/req/v3
  "github.com/imroc/req/v3"
  "log"${isContentTypeFormUrlEncoded ? '\n  "net/url"' : ''}
)

func main() {
  requestUrl := "${requestUrl}"
  client := req.C()

  ${bodyStringDesStr}

  resp, err := client.R().
    SetHeader("Content-Type", "${contentType}").
${isPrevilegePrivate ? authHeaderStr : ''}    SetBodyString(bodyString).
    Post(requestUrl)
  if err != nil {
    log.Fatal(err)
  }

  if !resp.IsSuccess() {
    fmt.Println("bad response status:", resp.Status)
    return
  }
  fmt.Println(resp.String())
}
`
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
        param.push(`data.Set("${itemName}", "${value}")`)
        break
      case DATA_TYPE.BIGINT:
        param.push(`data.Set("${itemName}", ${value})`)
        break
      case DATA_TYPE.DOUBLE:
        param.push(`data.Set("${itemName}", ${value})`)
        break
      case DATA_TYPE.DATE:
        param.push(`data.Set("${itemName}", "${value}")`)
        break
      case DATA_TYPE.ARRAY_STRING:
        if (values.length > 0) {
          values.forEach(el => {
            const effectValue = getEffectValue(itemType, el.va)
            param.push(`data.Add("${itemName}", "${effectValue}")`)
          })
        } else {
          param.push(`data.Add("${itemName}", "")`)
        }
        break
      case DATA_TYPE.ARRAY_BIGINT:
        if (values.length > 0) {
          values.forEach(el => {
            const effectValue = getEffectValue(itemType, el.va)
            param.push(`data.Add("${itemName}", "${effectValue}")`)
          })
        } else {
          param.push(`data.Add("${itemName}", 1)`)
        }
        break
      case DATA_TYPE.ARRAY_DOUBLE:
        if (values.length > 0) {
          values.forEach(el => {
            const effectValue = getEffectValue(itemType, el.va)
            param.push(`data.Add("${itemName}", "${effectValue}")`)
          })
        } else {
          param.push(`data.Add("${itemName}", 1.0")`)
        }
        break
      case DATA_TYPE.ARRAY_DATE:
        if (values.length > 0) {
          values.forEach(el => {
            const effectValue = getEffectValue(itemType, el.va)
            param.push(`data.Add("${itemName}", "${effectValue}")`)
          })
        } else {
          param.push(`data.Add("${itemName}", "1970-01-01 00:00:00")`)
        }
        break
      default:
        break
    }
  })
  return param
}