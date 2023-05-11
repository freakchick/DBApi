package com.gitee.freakchicken.dbapi.plugin.example;

import com.gitee.freakchicken.dbapi.common.ApiConfig;
import com.gitee.freakchicken.dbapi.plugin.AlarmPlugin;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;

/**
 * @description 飞书告警插件
 * @create 2023/3/8 18:03
 */
public class FeiShuAlarm extends AlarmPlugin {
    @Override
    public void init() {
    }

    @Override
    public void alarm(Exception e, ApiConfig config, HttpServletRequest request, String pluginParam) {
        String format = MessageFormat.format("InterfaceName：{0} \\n ErrorMassage：{1} \\nErrorURL: {2} \\nRemoteAddress: {3}",
                config.getName(), e.getMessage(),request.getRequestURI(),request.getRemoteAddr());

        String body = "{\"msg_type\":\"text\",\"content\":{\"text\":\"" + format + "\"}}";
        sendDingTalkMassage(pluginParam,body);
    }

    @Override
    public String getName() {
        return "飞书告警插件";
    }

    @Override
    public String getDescription() {
        return "使用该插件，异常信息会发送至飞书群";
    }

    @Override
    public String getParamDescription() {
        return "填写从飞书群中获取到的机器人WebHook";
    }

    public String sendDingTalkMassage(String webHook,String body){
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        CloseableHttpResponse response = null;
        HttpPost httpPost = new HttpPost(webHook);
        httpPost.setHeader("Content-Type", "application/json;charset=utf8");
        try {
            StringEntity stringEntity = new StringEntity(body,"utf-8");
            httpPost.setEntity(stringEntity);
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (httpClient != null) httpClient.close();
                if (response != null) response.close();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
