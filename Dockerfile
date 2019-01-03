FROM openjdk:10.0.2-jdk

WORKDIR /
EXPOSE 8080

ARG JAR_FILE
COPY ${JAR_FILE} app.jar

CMD java -jar app.jar