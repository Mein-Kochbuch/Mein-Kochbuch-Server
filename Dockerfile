FROM openjdk:17

MAINTAINER Florian Weber <info@mein-kochbuch.org>

ADD "backend/target/MeinKochbuch-0.0.1-SNAPSHOT.jar" server.jar

CMD [ "sh", "-c", "java -jar /server.jar" ]