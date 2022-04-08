package com.gitee.freakchicken.dbapi.basic.log;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;

public class AccessLogDBAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {



    protected String url;

    protected String username;
    protected String password;
    protected String driver;

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    @Override
    public void start() {
        super.start();
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++DB appender start");
        if (this.url == null) {
            addError("url blank");
            return;
        }
        if (this.username == null || username.length() == 0) {
            addError("username could not be blank.");
            return;
        }
    }

    @Override
    protected void append(ILoggingEvent event) {
        //TODO
        String content = event.getFormattedMessage();
        System.out.println("----------------" + this.username + this.url);
        System.out.println(content);
    }


}
