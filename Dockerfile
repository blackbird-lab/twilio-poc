FROM openjdk:10.0.2-jdk

WORKDIR /
EXPOSE 8080 8000

ARG JAR_FILE
COPY ${JAR_FILE} app.jar

CMD java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000 -jar app.jar