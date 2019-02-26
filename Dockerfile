FROM gcr.io/distroless/java:8

COPY target/mailsink.jar /

EXPOSE 2500 2525

CMD [ "/mailsink.jar" ]
