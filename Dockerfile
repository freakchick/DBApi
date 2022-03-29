FROM openjdk:8-jre-slim-buster
ARG DEBIAN_FRONTEND=noninteractive

ARG VERSION
ENV DOCKER true
ENV ROLE standalone

RUN apt-get update && \
    apt-get install -y --no-install-recommends tzdata dos2unix && \
    echo "Asia/Shanghai" > /etc/timezone && \
    rm -f /etc/localtime && \
    dpkg-reconfigure tzdata && \
    rm -rf /var/lib/apt/lists/* /tmp/*

ADD dist/DBApi-${VERSION}-bin.tar.gz /opt/

COPY docker/init_config.sh /opt/init_config.sh
COPY docker/startup.sh /opt/startup.sh

WORKDIR /opt/DBApi-${VERSION}

EXPOSE 8520 8523 8525

ENTRYPOINT sh /opt/startup.sh $ROLE