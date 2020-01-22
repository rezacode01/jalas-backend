FROM maven:3.6.1-jdk-8-alpine AS build-env

ENV LC_ALL=C

WORKDIR /app
COPY . /app
RUN mvn clean package

FROM java:8

COPY --from=build-env /app/target/jalas-0.0.1-SNAPSHOT.jar /data/app.jar
WORKDIR /data
RUN mkdir /tz && mv /etc/timezone /tz/ && mv /etc/localtime /tz/ && ln -s /tz/timezone /etc/ && ln -s /tz/localtime /etc/
RUN echo "Asia/Tehran" > /etc/timezone && dpkg-reconfigure -f noninteractive tzdata && cp /etc/localtime /tz/
VOLUME /tz
CMD exec java -jar app.jar