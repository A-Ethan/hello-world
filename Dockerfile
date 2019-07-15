FROM maven:3-jdk-8-alpine
WORKDIR /usr/src/app
COPY target /usr/src/app

ENV PORT 5000
EXPOSE $PORT
CMD [ "sh", "-c", "java -jar /usr/src/app/demo-0.0.1-SNAPSHOT.jar -Dserver.port=${PORT}" ]
