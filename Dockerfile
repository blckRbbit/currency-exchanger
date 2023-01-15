FROM openjdk:8-jdk-alpine

VOLUME /tmp

EXPOSE 9090

ARG JAR_FILE=build/libs/feign-0.0.1.jar

ADD ${JAR_FILE} feign-0.0.1.jar

ENTRYPOINT ["java","-jar","/feign-0.0.1.jar"]
