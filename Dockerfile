FROM openjdk:17-slim
COPY $DIR/binobs.jar /binobs.jar

CMD ["java", "-jar", "/binobs.jar"]