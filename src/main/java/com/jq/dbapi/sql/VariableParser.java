package com.jq.dbapi.sql;

import com.alibaba.fastjson.JSON;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @program: dbApi
 * @description:
 * @author: jiangqiang
 * @create: 2021-02-19 16:33
 **/
public class VariableParser {

    private static String openToken = "#{";
    private static String closeToken = "}";

    public static void main(String[] args) throws DocumentException {
//        String parse = parse("   and name = #{minId\\}} and id < #{yy \n} and name = #{ eee  }");
//        System.out.println(parse);

        parseVariableNames("select * from Blog where 1=1<if test=\"minId != null and minId != '' \">  and id > #{minId} </if><if test=\"maxId != null and maxId != '' \"> and id &lt; #{maxId} </if> \t<if test=\"minId != null and minId != '' \"> and id > #{minId} </if> and udr = #{  ffr}");
    }

    /**
     * 将sql中的参数替换成？ 并且将？对应的参数值按顺序保存起来
     *
     * @param text
     * @param params
     * @param jdbcParams
     * @return
     */
    public static String parseSql(String text, Map<String, Object> params, List<Object> jdbcParams) {
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

    /**
     * 解析sql中的变量名称
     *
     * @param text
     * @return
     */
    public static Set<String> parseVariableNamesFromSqlText(String text) {
        Set<String> set = new HashSet<>();
        if (text == null || text.isEmpty()) {
            return set;
        }
        int start = text.indexOf(openToken);
        if (start == -1) {
            return set;
        }
        char[] src = text.toCharArray();
        int offset = 0;
//        final StringBuilder builder = new StringBuilder();
        StringBuilder expression = null;
        do {
            //搜索到假的#{ ，  \#{ 转化成 #{
            if (start > 0 && src[start - 1] == '\\') {
//                builder.append(src, offset, start - offset - 1).append(openToken);
                offset = start + openToken.length();
            }
            //搜索到真实的 #{
            else {

                if (expression == null) {
                    expression = new StringBuilder();
                } else {
                    expression.setLength(0);
                }
//                builder.append(src, offset, start - offset);
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

//                    builder.append(src, start, src.length - start);
                    offset = src.length;
                }
                //搜索到真实的右括号}
                else {

//                    builder.append("?");
                    String paramName = expression.toString().trim();
                    set.add(paramName);
//                    jdbcParams.add(params.get(paramName));
                    offset = end + closeToken.length();
                }
            }
            start = text.indexOf(openToken, offset);
        } while (start > -1);
        if (offset < src.length) {
//            builder.append(src, offset, src.length - offset);
        }
        return set;
    }

    /**
     * 解析完整sql中的所有变量名
     * @param sql
     * @return
     * @throws DocumentException
     */
    public static Set<String> parseVariableNames(String sql) throws DocumentException {
        sql = String.format("<root>%s</root>", sql);
        List<Object> tags = DynamicSqlXmlBuilder.parseXml(sql);
        Set<String> collect = tags.parallelStream()
                .flatMap(t -> {
                    if (t instanceof String) {
                        return parseVariableNamesFromSqlText((String) t).stream();
                    } else if (t instanceof Element) {
                        String textTrim = ((Element) t).getTextTrim();
                        return parseVariableNamesFromSqlText(textTrim).stream();
                    }
                    return null;
                }).collect(Collectors.toSet());
        return collect;
    }

}