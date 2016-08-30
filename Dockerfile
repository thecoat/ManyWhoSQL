FROM maven:onbuild-alpine

EXPOSE 8080

CMD ["java", "-Xmx600m", "-jar", "/usr/src/app/target/sql-1.0-SNAPSHOT.jar"]
