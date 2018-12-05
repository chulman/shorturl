FROM java:openjdk-8-jdk

WORKDIR /shorturl/program/bootapp
ADD build/libs/app.jar app.jar

CMD ["/bin/bash", "-c", "exec java -Dspring.profiles.active=local $JAVA_OPTS -jar app.jar"]
#CMD ["/bin/bash", "-c", "exec java -Dspring.profiles.active=local $JAVA_OPTS -jar app.jar >> /Users/chul/log/shorturl.log 2>&1"]


# ./gradlew bootJar
# docker build -t  chulm/shorturl/app:latest ./
# docker run --name=redis -d -p 6379:6379 redis:latest
# docker run --name=shorturl-app -p 8080:8080 --link redis:redis -e JAVA_OPTS="-Dshort-url.service.url=localhost:8080" chulm/shorturl/app:latest