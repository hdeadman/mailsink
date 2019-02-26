call mvn -DskipTests package
docker build --tag devops/mailsink .
docker stop mailsink
docker run --rm -d -p 2525:2525 -p 2500:2500 --name mailsink devops/mailsink