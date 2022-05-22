echo "Compile and running function service ..."
cd src\main\resources\dockerDB\
docker build --tag function . & docker-compose up