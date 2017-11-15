FROM openjdk:8-jdk-alpine

MAINTAINER jm5619

RUN mkdir /app

WORKDIR /app

ADD ./target/ir-user-1.0.0-SNAPSHOT.jar /app

EXPOSE 8080

CMD ["java", "-jar", "ir-user-1.0.0-SNAPSHOT.jar"]
