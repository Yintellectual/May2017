docker ps
docker images

docker-compose down --rmi local

1. docker-compose up with newly compiled project

	"docker-compose up" uses the old image by default. 
	
	"docker image" shows all the images
	
	"docker-compose down --rmi local" kills old image.
	
	The old image must be killed before trying to run the new one.
	
2. docker exec -it  <docker container name> bash
	//open bash to the virtual machine
	
3. in docker-compose.yml, 

services:
  web:
    build: .
    ports:
      - "443:8443"
      - "80:8080"
    volumes:
      - ./externaldata:/usr/local/tomcat/webapps/ROOT/uploads/

	  will create a portal that passes anything in ./externaldata to "uploads" in the vm
	  
	  However, the jsp file can not be passed directly for tomcat. You can pass it to the vm first, then transfer it into the tomcat folder.