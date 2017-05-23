Today theres three things to do:

1. Write a rabbitMQ POC project

2. Write a selectively update repository POC project

3. Learn UML from the introduction provided by some blogger from IBM
		https://www.ibm.com/developerworks/rational/library/769.html?ca=drs-

		
Basically, I can use the same project to sample both rabbitMQ and selectively-updatable-repository.

This project depends on a local rabbitMQ server, and contains three componets:

The first component is the Entity POJOs. These POJO is considered common accoss the all the applications of the whole project.
Practically, they are the protocal of inter-application communication. 

The second component is the client application.

The third component is the service application that is backed by an inmemory database. 

The design of such an project should be made in UML. The development should restrictively follow the code of Test-Driven-Development.

Timetable（total 12 hours）: 

10
{
	1 hour: Reviwement of Spring Data and setting up service application
}
11
{
	1 hour: read uml introduction and leaving notes
}
12
{
	1 hour: POC project design
}

1: Lunch
//
2
{
	2 hour: test-driven selectively-updatable-repository development
}
4
{
	1 hour: setting up client application and communicate service through restful api
}
5: Refreshment and rest 
//
6
//
7: Coffee
{
	1 hour: read about concepts from rabbitMQ 
}
{
	1 hour: implement inter-application communicate through rabbitMQ
}
9

10:Dinner
//

Works:
count: 8 hours in total