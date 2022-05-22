FROM openjdk:11.0.14.1-jre
WORKDIR /api/v1/function
COPY target/demo-0.0.1-SNAPSHOT.jar demo-0.0.1-SNAPSHOT.jar