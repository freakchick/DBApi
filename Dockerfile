FROM freakchicken/java8:1.0

ARG VERSION
ENV DOCKER true

ADD dist/DBApi-${VERSION}-bin.tar.gz /opt/

WORKDIR /opt/DBApi-${VERSION}

COPY docker/init_config.sh /opt/init_config.sh

# 环境变量替换成spring认识的格式
RUN sh /opt/init_config.sh

EXPOSE 8520 8523 8524 8525

ENTRYPOINT sh bin/dbapi.sh start standalone