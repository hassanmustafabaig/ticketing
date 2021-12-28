FROM openjdk:11
FROM maven:3.6.3-jdk-11 AS builder
MAINTAINER hassan.mustafa@venturedive.com

COPY pom.xml /usr/local/pom.xml
COPY src /usr/local/src

WORKDIR /usr/local/

RUN mvn clean install

COPY target/ticketing-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8085

ENTRYPOINT ["java","-Dspring.profiles.active=dev","-jar","/app.jar"]