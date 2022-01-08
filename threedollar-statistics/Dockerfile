FROM adoptopenjdk/openjdk11:alpine-slim AS BUILD
ENV HOME=/usr/app
WORKDIR $HOME
COPY . $HOME
RUN ./gradlew clean :threedollar-statistics:bootJar
:threedollar-statistics:test \
:threedollar-common:test

FROM adoptopenjdk/openjdk11:alpine-jre
ENV HOME=/usr/app
COPY --from=BUILD  $HOME/threedollar-admin/build/libs/threedollar-statistics.jar /threedollar-statistics.jar
EXPOSE 5200

ENTRYPOINT ["java", "-jar", "-Duser.timezone=Asia/Seoul", "/threedollar-statistics.jar"]
