package com.gitee.freakchicken.dbapi.plugin.impl;

import com.gitee.freakchicken.dbapi.common.ApiConfig;
import com.gitee.freakchicken.dbapi.plugin.AlarmPlugin;
import com.gitee.freakchicken.dbapi.plugin.PluginConf;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 邮件告警插件
 */
public class EmailAlarmPlugin extends AlarmPlugin {

    private String username;
    private String password;
    private String host;

    @Override
    public void init() {
        this.username = PluginConf.getKey("EMAIL_USERNAME");
        this.password = PluginConf.getKey("EMAIL_PASSWORD");
        this.host = PluginConf.getKey("EMAIL_HOST");
    }

    @Override
    public void alarm(Exception e, ApiConfig config, HttpServletRequest request, String pluginParam) {

        String title = MessageFormat.format("API ERROR: {0}", config.getName());
        String content = MessageFormat.format("TIME:  {0}\n API_ID:  {5}\nNAME:  {1}\n URL:  {2}\n REMOTE ADDRESS:  {3}\n\n{4}",
                new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS").format(new Date()),
                config.getName(), request.getRequestURI(), request.getRemoteAddr(), e.toString(), config.getId());
        sendSimpleTextEmail(pluginParam, title, content);
    }

    public void sendSimpleTextEmail(String to, String title, String content) {
        try {
            Email email = new SimpleEmail();
            email.setHostName(this.host);

//        email.setSmtpPort(465);
            email.setAuthenticator(new DefaultAuthenticator(this.username, this.password));
            email.setSSLOnConnect(true);
            email.setCharset("UTF-8");

            email.setFrom(this.username);

            email.addTo(to);
            email.setSubject(title);
            email.setMsg(content);
            email.send();
        } catch (EmailException e) {
            super.logger.info(e.getMessage(), e);
        }

    }

    @Override
    public String getName() {
        return "邮件告警插件";
    }

    @Override
    public String getDescription() {
        return "使用该插件，异常信息会发送邮件通知";
    }

    @Override
    public String getParamDescription() {
        return "插件参数请填写收件人邮箱，多个用英文分号;隔开";
    }
}
