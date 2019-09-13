FROM java:openjdk-8-jdk

MAINTAINER cmchoi, chlcjfals0122@gmail.com
LABEL com.example.version = "0.0.1-SNAPSHOT"

ENV JAVA_OPTS -Xms1024m -Xmx1024m -XX:+HeapDumpOnOutOfMemoryError

WORKDIR /program/bootapp
ADD build/libs/app.jar app.jar

EXPOSE 8080

#ENTRYPOINT ["/bin/bash", "-c", "exec java $SPRING_OPTS $JAVA_OPTS -jar app.jar" ]

#CMD ["/bin/bash", "-c", "exec java $SPRING_OPTS $JAVA_OPTS -jar app.jar" ]
CMD ["/bin/bash", "-c", "exec java -Dspring.profiles.active=production $JAVA_OPTS -jar app.jar"]



# docker run --name=myapp -p 8080:8080 --link redis:redis -e SPRING_OPTS="-Dspring.profiles.active=local" chulm/myapp:latest