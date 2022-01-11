FROM adoptopenjdk/openjdk11:alpine-slim AS BUILD
ENV HOME=/usr/app
WORKDIR $HOME
COPY . $HOME
RUN ./gradlew clean :threedollar-boss-api:bootJar \
:threedollar-boss-api:test \
:threedollar-boss-domain:test \
:threedollar-common:test

FROM adoptopenjdk/openjdk11:alpine-jre
ENV HOME=/usr/app
COPY --from=BUILD  $HOME/threedollar-boss-api/build/libs/threedollar-boss-api.jar /threedollar-boss-api.jar
EXPOSE 4000

ENTRYPOINT ["java", "-jar", "-Duser.timezone=Asia/Seoul", "/threedollar-boss-api.jar"]
