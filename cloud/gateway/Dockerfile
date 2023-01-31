FROM bellsoft/liberica-openjre-alpine:17
VOLUME /tmp
COPY target/app.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
