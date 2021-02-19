package com.jq.dbapi.sql;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @program: dbApi
 * @description:
 * @author: jiangqiang
 * @create: 2021-02-19 16:33
 **/
public class VariableParser {

    private static String openToken = "#{";
    private static String closeToken = "}";

    public static void main(String[] args) {
//        String parse = parse("   and name = #{minId\\}} and id < #{yy \n} and name = #{ eee  }");
//        System.out.println(parse);
    }

    /**
     * 将sql中的参数替换成？ 并且将？对应的参数值按顺序保存起来
     * @param text
     * @param params
     * @param jdbcParams
     * @return
     */
    public static String parse(String text, Map<String,Object> params, List<Object> jdbcParams) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        int start = text.indexOf(openToken);
        if (start == -1) {
            return text;
        }
        char[] src = text.toCharArray();
        int offset = 0;
        final StringBuilder builder = new StringBuilder();
        StringBuilder expression = null;
        do {
            //搜索到假的#{ ，  \#{ 转化成 #{
            if (start > 0 && src[start - 1] == '\\') {
                builder.append(src, offset, start - offset - 1).append(openToken);
                offset = start + openToken.length();
            }
            //搜索到真实的 #{
            else {

                if (expression == null) {
                    expression = new StringBuilder();
                } else {
                    expression.setLength(0);
                }
                builder.append(src, offset, start - offset);
                offset = start + openToken.length();

                //开始搜索 }
                int end = text.indexOf(closeToken, offset);
                while (end > -1) {
                    //搜索到假的 } ，   \} 转化成 }
                    if (end > offset && src[end - 1] == '\\') {
                        expression.append(src, offset, end - offset - 1).append(closeToken);
                        offset = end + closeToken.length();
                        //继续向右搜索 }
                        end = text.indexOf(closeToken, offset);
                    }
                    //搜索到真实的 }
                    else {
                        expression.append(src, offset, end - offset);
                        break;
                    }
                }
                //没有搜索到真实的右括号 }
                if (end == -1) {

                    builder.append(src, start, src.length - start);
                    offset = src.length;
                }
                //搜索到真实的右括号}
                else {

                    builder.append("?");
                    String paramName = expression.toString().trim();
                    jdbcParams.add(params.get(paramName));
                    offset = end + closeToken.length();
                }
            }
            start = text.indexOf(openToken, offset);
        } while (start > -1);
        if (offset < src.length) {
            builder.append(src, offset, src.length - offset);
        }
        return builder.toString();
    }
}