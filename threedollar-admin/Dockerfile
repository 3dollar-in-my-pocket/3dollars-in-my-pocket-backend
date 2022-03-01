FROM adoptopenjdk/openjdk11:alpine-slim AS BUILD
ENV HOME=/usr/app
WORKDIR $HOME
COPY . $HOME
RUN ./gradlew clean :threedollar-admin:buildNeeded

FROM adoptopenjdk/openjdk11:alpine-jre
ENV HOME=/usr/app
COPY --from=BUILD  $HOME/threedollar-admin/build/libs/threedollar-admin.jar /threedollar-admin.jar
EXPOSE 5100

ENTRYPOINT ["java", "-XX:CompileThreshold=100", "-XX:+TieredCompilation", "-Duser.timezone=Asia/Seoul", "-jar", "/threedollar-admin.jar"]
