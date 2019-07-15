FROM maven:3-jdk-8-alpine

ENV PORT 5000
EXPOSE $PORT
CMD [ "sh", "-c", "javar -jar demo-0.0.1-SNAPSHOT.jar -Dserver.port=${PORT}" ]
