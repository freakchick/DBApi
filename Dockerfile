FROM openjdk:8-jre-slim-buster

ARG DEBIAN_FRONTEND=noninteractive
ARG VERSION

ENV DOCKER true

ADD dist/dbapi-${VERSION}-bin.tar.gz /opt/

COPY docker/init_config.sh /opt/init_config.sh
COPY docker/startup.sh /opt/startup.sh

WORKDIR /opt/dbapi-${VERSION}

EXPOSE 8520 8523 8525

ENTRYPOINT bash /opt/startup.sh $0