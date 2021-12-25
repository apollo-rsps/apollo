FROM openjdk:11

WORKDIR /usr/server

COPY . .

CMD ./gradlew run
