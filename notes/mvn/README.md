Guide to installing 3rd party JARs

https://maven.apache.org/guides/mini/guide-3rd-party-jars-local.html
	
	#packaging can be jar or pom, all parameters can be found in POM.xml
	mvn install:install-file -Dfile=<path-to-file> -DgroupId=<group-id> \
	-DartifactId=<artifact-id> -Dversion=<version> -Dpackaging=<packaging>
	
	