echo "Compile and running function service ..."
cd src\main\resources\dockerDB\
docker build --tag function . & mvnw.cmd clean install & docker-compose up