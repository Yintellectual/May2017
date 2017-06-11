Suppose a POJO PojoExample is to be validated. 

1. in PojoExample.java, 

		

2. in controller

	the bottom line is to make sure both models exist:
	
		1. "": PojoExample
		
		2. "": ResultBinding
		
	Ususally it can be simply done by (@Valid PojoExample, ResultBinding)

3. in JSP, both spring form taglib and themeleaf taglib can do the job. 