FROM adoptopenjdk/openjdk11:alpine-slim AS BUILD
ENV HOME=/usr/app
WORKDIR $HOME
COPY . $HOME
RUN ./gradlew clean :threedollar-api-boss:buildNeeded --parallel

FROM adoptopenjdk/openjdk11:alpine-jre
ENV HOME=/usr/app
COPY --from=BUILD  $HOME/threedollar-api-boss/build/libs/threedollar-api-boss.jar /threedollar-api-boss.jar
EXPOSE 4000

ENTRYPOINT ["java", "-XX:CompileThreshold=100", "-XX:+TieredCompilation", "-Duser.timezone=Asia/Seoul", "-jar", "/threedollar-api-boss.jar"]
