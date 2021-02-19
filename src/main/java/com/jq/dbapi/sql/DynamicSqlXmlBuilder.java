package com.jq.dbapi.sql;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.ibatis.ognl.Ognl;
import org.apache.ibatis.ognl.OgnlContext;
import org.apache.ibatis.ognl.OgnlException;
import org.dom4j.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 解析xml中的动态sql
 *
 * @program: dbApi
 * @description:
 * @author: jiangqiang
 * @create: 2021-02-19 13:59
 **/
@Slf4j
public class DynamicSqlXmlBuilder {

    public static void main(String[] args) throws Exception {
        String file = FileUtils.readFileToString(new File("C:\\Users\\lty-017\\Desktop\\a.xml"), "utf-8");
        List<Object> list = parseXml(file);
        Map<String, Object> map = new HashMap<>();
        map.put("minId", 100);
        map.put("maxId", 500);
        buildSql(list, map);

    }

    /**
     * 解析xml中的纯文本和标签
     *
     * @param content
     * @return
     * @throws DocumentException
     */
    public static List<Object> parseXml(String content) throws DocumentException {
        List<Object> res = new ArrayList<>();
        Document document = DocumentHelper.parseText(content);
        Element rootElement = document.getRootElement();
        List nodes = rootElement.content();
        for (int i = 0; i < nodes.size(); i++) {
            Object o = nodes.get(i);
            if (o instanceof Text) {
                Text text = (Text) o;
                res.add(text.getText().trim());
            }
            if (o instanceof Element) {
                Element element = (Element) o;
                res.add(element);
            }
        }
        return res;
    }

    public static SqlExecutor buildSql(List<Object> sqlElements, Map<String, Object> params) throws Exception {
        StringBuilder sqlBuffer = new StringBuilder();
        List<Object> jdbcParams = new ArrayList<>();
        for (int i = 0; i < sqlElements.size(); i++) {
            Object o = sqlElements.get(i);

            if (o instanceof String) {
                String parse = VariableParser.parse((String) o, params, jdbcParams);
                sqlBuffer.append(parse).append(" ");
            }
            if (o instanceof Element) {
                Element element = (Element) o;

                String s = parseDynamicTag(element, params, jdbcParams);
                sqlBuffer.append(s).append(" ");
            }
        }
        log.debug(sqlBuffer.toString());
        log.debug(JSON.toJSONString(jdbcParams));
        SqlExecutor sqlExecutor = new SqlExecutor(sqlBuffer.toString(), jdbcParams);
        return sqlExecutor;
    }

    /**
     * 解析动态标签，生成sql段并且保存好jdbc参数
     *
     * @param element
     * @param params
     * @param jdbcParams
     * @return
     * @throws OgnlException
     */
    public static String parseDynamicTag(Element element, Map<String, Object> params, List<Object> jdbcParams) throws Exception {
        String name = element.getName();
        String textTrim = element.getTextTrim();
        if (name.equals("if")) {
            String expression = element.attributeValue("test");

            OgnlContext context = new OgnlContext(null, null, new DefaultMemberAccess(true));
            Object value = Ognl.getValue(Ognl.parseExpression(expression), context, params);
            if (value instanceof Boolean) {
                Boolean b = (Boolean) value;
                if (b) {
                    String parse = VariableParser.parse(textTrim, params, jdbcParams);
                    return parse;
                } else {
                    return "";
                }
            } else {
                throw new RuntimeException("<if>标签test表达式错误");
            }
        } else {
            throw new RuntimeException("不支持的标签：" + name);
        }

    }
}
