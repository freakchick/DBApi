FROM freakchicken/java8:1.0

ARG VERSION
ENV DOCKER true
ENV ROLE standalone

ADD dist/DBApi-${VERSION}-bin.tar.gz /opt/

COPY docker/init_config.sh /opt/init_config.sh
COPY docker/startup.sh /opt/startup.sh

WORKDIR /opt/DBApi-${VERSION}

EXPOSE 8520 8523 8524 8525

ENTRYPOINT sh /opt/startup.sh $ROLE