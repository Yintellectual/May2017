1. default rules followed by spring data rest 

	?
	
2. customize spring data rest to achieve selectively updatable repository

	?
	
3. set base url

		spring.data.rest.baseUri=api
		
4. set custom url 

		@RestResource(path = "names", rel = "names")
		someMethod()