import { DATA_TYPE, CONTENT_TYPE } from "@/constant";

export function generateJavaCallExampleCode(param) {

  const {
    lang,
    address, requestUrl,
    isContentTypeJson, isContentTypeFormUrlEncoded, contentType,
    isPrevilegePrivate, isPrevilegePublic, token,
    params,
    detail
  } = param

  let dataDesStr = ''
  if (isContentTypeJson) {
    dataDesStr = `String dataStr = "${detail.jsonParam.replace(/\n|\r|\t/g, '').replace(/"/g, '\\"')}";`
  } else if (isContentTypeFormUrlEncoded) {
    dataDesStr = generateUrlEncodedDataDesStr(param)
  }

  return `import java.io.*;
import java.net.*;${isContentTypeFormUrlEncoded ? '\nimport java.util.*;\nimport java.util.stream.Collectors;' : ''}

public class Main {
    public static void main(String[] args) throws IOException {
        String requestUrl ="${requestUrl}";

        URL url = new URL(requestUrl);

        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("POST");
        connection.setUseCaches(false);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "${contentType}");${isPrevilegePrivate ? '        connection.setRequestProperty("Authorization", "");\n' : ''}
        
        connection.connect();

        ${dataDesStr}

        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(),"UTF-8");
        writer.write(dataStr);
        writer.flush();
        writer.close();

        InputStream is = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        StringBuffer stringBuffer = new StringBuffer();
        String strRead;
        while ((strRead = reader.readLine()) != null) {
            stringBuffer.append(strRead);
            stringBuffer.append("\\r\\n");
        }
        reader.close();
        connection.disconnect();
        System.out.println(stringBuffer);
    }
}`
}

function generatePrarmCode(params) {
  const codes = []
  params.forEach(item => {
    const itemType = item.type
    const itemName = item.name
    switch (itemType) {
      case DATA_TYPE.STRING:
        codes.push(`params.put("${itemName}", "");`)
        break
      case DATA_TYPE.BIGINT:
        codes.push(`params.put("${itemName}", 0L);`)
        break
      case DATA_TYPE.DOUBLE:
        codes.push(`params.put("${itemName}", 1D);`)
        break
      case DATA_TYPE.DATE:
        codes.push(`params.put("${itemName}", "1970-01-01 00:00:00");`)
        break
      case DATA_TYPE.ARRAY_STRING:
        codes.push(`List<String> ${itemName}Array = new ArrayList<String>();`)
        codes.push(`${itemName}Array.add("");`)
        codes.push(`params.put("${itemName}", ${itemName}Array);`)
        break
      case DATA_TYPE.ARRAY_BIGINT:
        codes.push(`List<Long> ${itemName}Array = new ArrayList<Long>();`)
        codes.push(`${itemName}Array.add(1L);`)
        codes.push(`params.put("${itemName}", ${itemName}Array);`)
        break
      case DATA_TYPE.ARRAY_DOUBLE:
        codes.push(`List<Double> ${itemName}Array = new ArrayList<Double>();`)
        codes.push(`${itemName}Array.add(1D);`)
        codes.push(`params.put("${itemName}", ${itemName}Array);`)
        break
      case DATA_TYPE.ARRAY_DATE:
        codes.push(`List<String> ${itemName}Array = new ArrayList<String>();`)
        codes.push(`${itemName}Array.add("1970-01-01 00:00:00");`)
        codes.push(`params.put("${itemName}", ${itemName}Array);`)
        break
      default:
        break
    }
  })
  return codes
}

function generateUrlEncodedDataDesStr({
  lang,
  address, requestUrl,
  isContentTypeJson, isContentTypeFormUrlEncoded, contentType,
  isPrevilegePrivate, isPrevilegePublic, token,
  params,
  detail
}) {
  const codes = generatePrarmCode(params)
  return `HashMap<String, Object> params = new HashMap<>();
        ${codes.join('\n        ')}

        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, Object> entry : params.entrySet()){
          if (first){
              first = false;
          } else {
              result.append("&");
          }
          String key = entry.getKey();
          Object value = entry.getValue();
          if(value instanceof List){
              List<String> list = (List<String>)value;
              String collectResult = list.stream().map(s -> {
                  try {
                      return URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(s, "UTF-8");
                  } catch (UnsupportedEncodingException e) {
                      e.printStackTrace();
                      return null;
                  }
              }).filter(Objects::nonNull).collect(Collectors.joining("&"));
              result.append(collectResult);
          }else if(value instanceof String){
              result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
              result.append("=");
              result.append(URLEncoder.encode((String) entry.getValue(), "UTF-8"));
          }
        }
        String dataStr = result.toString();`
}