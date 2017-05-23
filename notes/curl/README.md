1. Always quote the strings when using curl from cmd, otherwise errors may occur. For example, this is fine:
 
	curl "localhost:8080/people/search/setFirstNameForPerson?firstName=Peter&lastName=Baggins"
	
While this is not:
 
	curl localhost:8080/people/search/setFirstNameForPerson?firstName=Peter&lastName=Baggins
	
	
2. Commonly used patterns:

	2.1 using url parameter:
	
	curl localhost:8080/people/search/setFirstNameForPerson?firstName=Peter&lastName=Baggins
	
	2.2 passing Json body:
	
	curl -i -X POST -H "Content-Type:application/json" -d "{  \"firstName\" : \"Frodo\",  \"lastName\" : \"Baggins\" }" http://localhost:8080/people