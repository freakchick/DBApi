FROM freakchicken/java8:1.0

ARG VERSION
ENV DOCKER true
ENV ROLE standalone

ADD dist/DBApi-${VERSION}-bin.tar.gz /opt/

WORKDIR /opt/DBApi-${VERSION}

EXPOSE 8520 8523 8524 8525

ENTRYPOINT sh bin/dbapi.sh start $ROLE