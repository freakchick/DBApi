FROM openjdk:8-jre-slim-buster

ARG DEBIAN_FRONTEND=noninteractive
ARG VERSION

ENV DOCKER true
ENV ROLE standalone

ADD dist/DBApi-${VERSION}-bin.tar.gz /opt/

COPY docker/init_config.sh /opt/init_config.sh
COPY docker/startup.sh /opt/startup.sh

WORKDIR /opt/DBApi-${VERSION}

EXPOSE 8520 8523 8525

ENTRYPOINT sh /opt/startup.sh $ROLE