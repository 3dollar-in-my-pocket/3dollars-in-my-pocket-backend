FROM adoptopenjdk/openjdk11:alpine-slim AS BUILD
ENV HOME=/usr/app
WORKDIR $HOME
COPY . $HOME
RUN ./gradlew clean :threedollar-batch:bootJar \
:threedollar-batch:test \
:threedollar-domain:test \
:threedollar-external:test

FROM adoptopenjdk/openjdk11:alpine-jre
ENV HOME=/usr/app
COPY --from=BUILD  $HOME/threedollar-batch/build/libs/threedollar-batch.jar /threedollar-batch.jar

ENTRYPOINT ["java", "-jar", "-Duser.timezone=Asia/Seoul", "/threedollar-batch.jar"]
