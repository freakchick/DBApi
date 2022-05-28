import { DATA_TYPE, CONTENT_TYPE } from "@/constant";

export function generateGoCallExampleCode({
  lang,
  address, requestUrl,
  isContentTypeJson, isContentTypeFormUrlEncoded, contentType,
  isPrevilegePrivate, isPrevilegePublic, token,
  params,
  detail
}) {

  const authHeaderStr = `    SetHeader("Authorization", "${token}").\n`

  let bodyStringDesStr = ''
  if (isContentTypeJson) {
    bodyStringDesStr = `\n  bodyString := "${detail.jsonParam.replace(/\n|\r|\t/g, '').replace(/"/g, '\\"')}"\n`
  } else if (isContentTypeFormUrlEncoded) {
    const param = getParam(params)
    bodyStringDesStr = `data := url.Values{}
  ${param.join("\n  ")}
  bodyString := data.Encode()`
  }

  return `package main
import (
  "fmt"
  // go get -u github.com/imroc/req/v3
  "github.com/imroc/req/v3"
  "log"${isContentTypeFormUrlEncoded ? '\n"net/url"' : ''}
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

function getParam(params) {
  const param = []
  params.forEach(item => {
    const itemType = item.type
    const itemName = item.name
    switch (itemType) {
      case DATA_TYPE.STRING:
        param.push(`data.Set("${itemName}", "")`)
        break
      case DATA_TYPE.BIGINT:
        param.push(`data.Set("${itemName}", 0)`)
        break
      case DATA_TYPE.DOUBLE:
        param.push(`data.Set("${itemName}", 1.0)`)
        break
      case DATA_TYPE.DATE:
        param.push(`data.Set("${itemName}", "1970-01-01 00:00:00")`)
        break
      case DATA_TYPE.ARRAY_STRING:
        param.push(`data.Add("${itemName}", "")`)
        break
      case DATA_TYPE.ARRAY_BIGINT:
        param.push(`data.Add("${itemName}", 1)`)
        break
      case DATA_TYPE.ARRAY_DOUBLE:
        param.push(`data.Add("${itemName}", 1.0")`)
        break
      case DATA_TYPE.ARRAY_DATE:
        param.push(`data.Add("${itemName}", "1970-01-01 00:00:00")`)
        break
      default:
        break
    }
  })
  return param
}