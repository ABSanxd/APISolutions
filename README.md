# Ejecutar la api com maven - asegurarse que la version 
# de java en el pom concida con la instalada
.\mvnw.cmd spring-boot:run

# Crear imagen docker
docker build -t img-woof .

# Crear y ejecutar contenedor docker
docker run -d -p 8080:8080 --name cnt-woof img-woof

# Detener y eliminar el contenedor
docker stop cnt-woof && docker rm cnt-woof