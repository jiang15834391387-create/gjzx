#FROM findepi/graalvm:java17-native
FROM openjdk:17

MAINTAINER Lion Li

RUN mkdir -p /smartlink/server/logs \
    /smartlink/server/temp \
    /smartlink/skywalking/agent

WORKDIR /smartlink/server

ENV SERVER_PORT=8080 LANG=C.UTF-8 LC_ALL=C.UTF-8 JAVA_OPTS=""

EXPOSE ${SERVER_PORT}

ADD ./target/smartlink-admin.jar ./app.jar

ENTRYPOINT java -Djava.security.egd=file:/dev/./urandom -Dserver.port=${SERVER_PORT} \
           # 应用名称 如果想区分集群节点监控 改成不同的名称即可
           #-Dskywalking.agent.service_name=smartlink-server \
           #-javaagent:/smartlink/skywalking/agent/skywalking-agent.jar \
           -XX:+HeapDumpOnOutOfMemoryError -XX:+UseZGC ${JAVA_OPTS} \
           -jar app.jar

