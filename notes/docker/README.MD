docker ps
docker images

docker-compose down --rmi local

1. docker-compose up with newly compiled project

	"docker-compose up" uses the old image by default. 
	
	"docker image" shows all the images
	
	"docker-compose down --rmi local" kills old image.
	
	The old image must be killed before trying to run the new one.