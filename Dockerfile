FROM openjdk:17

MAINTAINER Florian Weber <info@mein-kochbuch.org>

ADD "./server.jar" server.jar

CMD [ "sh", "-c", "java -jar /server.jar" ]
