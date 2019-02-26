FROM gcr.io/distroless/java:11

COPY target/mailsink.jar /

EXPOSE 2500 2525

USER 1000

CMD [ "/mailsink.jar" ]
