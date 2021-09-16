FROM openjdk:11.0.11-jre

WORKDIR /home/workspace

COPY build/libs/message-broker-0.0.1-SNAPSHOT.jar .

ENTRYPOINT ["java","-jar","message-broker-0.0.1-SNAPSHOT.jar"]
