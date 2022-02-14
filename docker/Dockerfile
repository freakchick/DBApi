FROM openjdk:8-jre-slim-buster

ARG VERSION
ENV DOCKER true

ADD ./DBApi-${VERSION}-bin.tar.gz /opt/
WORKDIR /opt/DBApi-${VERSION}-bin


COPY ./init_config.sh /opt/init_config.sh

# 环境变量替换成spring认识的格式
RUN sh /opt/init_config.sh

EXPOSE 8520 8523 8524 8525

ENTRYPOINT sh bin/dbapi.sh start $1