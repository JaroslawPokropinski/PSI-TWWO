FROM node AS frontend
WORKDIR /usr/app
ADD ./ /usr/app
WORKDIR /usr/app/frontend
RUN npm install
RUN npm run build

FROM openjdk
WORKDIR /usr/app
COPY --from=frontend /usr/app/ .
RUN cp -a /usr/app/frontend/build/ ./src/main/resources/static

RUN chmod u+x ./gradlew
RUN ./gradlew build -x test
ADD ./ /usr/app
RUN chmod 777 /usr/app/build/libs/psi-0.0.1-SNAPSHOT.jar
CMD [ "java", "-jar", "/usr/app/build/libs/psi-0.0.1-SNAPSHOT.jar"  ]