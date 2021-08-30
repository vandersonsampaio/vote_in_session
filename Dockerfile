FROM openjdk:8-jdk-alpine
MAINTAINER vote-session
RUN mkdir /apps
COPY build/libs/vote-in-session-1.0-SNAPSHOT.jar /apps/vote-session.jar
ENTRYPOINT ["java", "-jar", "/apps/vote-session.jar"]