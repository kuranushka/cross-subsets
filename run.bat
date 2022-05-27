echo "Compile and running function service ..."
docker build --tag function . & mvnw.cmd clean install & docker-compose up
