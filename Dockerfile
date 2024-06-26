FROM openjdk:21

COPY target/booking-application-1.0-SNAPSHOT.jar booking-applications.jar

CMD ["java","-jar","booking-applications.jar"]
