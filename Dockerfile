FROM maven:3-jdk-8-alpine
WORKDIR /usr/src/app
COPY target /usr/src/app
COPY lib/jmx_prometheus_javaagent-0.12.0.jar /usr/src/app
COPY yaml/javaagent.yaml /usr/src/app

ENV PORT 8080
EXPOSE $PORT
ENV JMXPORT 9090
EXPOSE $JMXPORT
CMD [ "sh", "-c", "java -javaagent:jmx_prometheus_javaagent-0.12.0.jar=${JMXPORT}:javaagent.yaml   -jar /usr/src/app/demo-0.0.1-SNAPSHOT.jar -Dserver.port=${PORT} --spring.config.location=/etc/appconfig/hello.properties" ]
