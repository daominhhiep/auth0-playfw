docker build -t auth0-scala-web-app .
docker run --env-file .env -p 3000:3000 -it auth0-scala-web-app
