FROM frekele/ant:1.10-jdk8 as builder

WORKDIR /opt/cornipickle

COPY build.xml  .
COPY config.xml  .

RUN ant download-deps

RUN ant install-deps

COPY ./Source/ ./Source/

RUN ant

RUN ant test




FROM java:8-alpine

WORKDIR /opt/cornipickle

COPY --from=builder /opt/cornipickle/cornipickle.jar /opt/cornipickle/

EXPOSE 10101

ENTRYPOINT [ "java", "-jar", "cornipickle.jar" ]
