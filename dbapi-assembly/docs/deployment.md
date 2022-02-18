# 部署教程

```shell
docker run -it \
-e ROLE=standalone \
-p 8520:8520 \
-e DB_URL="jdbc:mysql://192.168.xx.xx:3306/dbapi?useSSL=false&characterEncoding=UTF-8&serverTimezone=GMT%2B8" \
-e DB_USERNAME="root" \
-e DB_PASSWORD="root" \
-e DB_DRIVER="com.mysql.cj.jdbc.Driver" \
freakchicken/db-api:3.0.0
```

```shell
docker run -it \
-e ROLE=manager \
-p 8523:8523 \
-e DB_URL="jdbc:mysql://192.168.xx.xx:3306/dbapi?useSSL=false&characterEncoding=UTF-8&serverTimezone=GMT%2B8" \
-e DB_USERNAME="root" \
-e DB_PASSWORD="root" \
-e DB_DRIVER="com.mysql.cj.jdbc.Driver" \
-e NACOS_ADDRESS="192.168.xx.xx:8848" \
freakchicken/db-api:3.0.0
```