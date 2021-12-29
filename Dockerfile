FROM openjdk:11

MAINTAINER hassan.mustafa@venturedive.com

ADD target/ticketing-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8085

ENTRYPOINT ["java","-Dspring.profiles.active=dev","-jar","app.jar"]