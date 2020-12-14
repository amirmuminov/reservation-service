FROM openjdk:11
MAINTAINER Amir Muminov
ADD /target/reservation-service-0.0.1-SNAPSHOT.jar reservation-service-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "reservation-service-0.0.1-SNAPSHOT.jar"]

EXPOSE 8084
