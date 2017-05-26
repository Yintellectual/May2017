UML, since 1997, is a language supported by diagrams. The uml diagrams are listed below:

reference: 

	https://www.ibm.com/developerworks/rational/library/content/RationalEdge/sep04/bell/index.html?ca=drs-

Online tool:
	https://dashboard.genmymodel.com/projectProperties/_Q2TcYD9wEeeTJ_4Vl2J2rQ
	authentication is acquired from github.com

1. 

	@Behavioral 
	use case diagrams

2. 

	@Structural 
	class

3. 

	@Behavioral 
	sequence

4. 

	@Structural 
	statechart

5. 

	@Behavioral 
	activity 

6. 

	@Structural 
	component

7. 

	@Structural 
	deployment



1. use case diagrams

	A use case diagrams shows system responsibilities(In Oval), 
	system users(custom drawing) and the mapping from users to system responsibilities 

2. class

	A class diagram shows the static OO structure of the system. It consists classes and the relatonships between classes.
	
	Classes are represented in three parts: name, attributes list, and operations list.
	
	An attribute in class diagram can have three kinds of information:
	
		name : type [= default value]
		
	An operation in class diagram can have four kinds of information:
	
		name([in] parameter-name: parameter-type):[out] return-type
	
	The relationship between classes are 
	
	1. Inheritance
	
	2. Bi-directional Relationship
	
		The bottom line is that if and only if a class knows about another, then the both the role and the multiplicity value 
		of the known class should be provided. 
		
	3. Uni-directional Relationship
	
	4. Association: classes that associate with some relationships, represented by dots without arrow.
	
	5. Aggregation: classes that is part of someother classes. represented by an arrow from the mother class to the part class.
		An diamond at the mother class side indicates the arrow to be an aggregation relationship.
	
		5.1 Basic aggregation means the part class's life circle is independent from the mother class's. It's diamond is white.
		
		5.2 Composition aggregation means the part class share the same life circle with the mother class. It's diamond is black.
		
	6. Reflective relationship
	
	Visibility: +, -, #(protected), ~(package)
	
	UML 2.0
	
		1. Instance:
		
			instance-name:type
			attribute-name:attribute-type:attribute-value
			
		2. Roles:

			role-name:type
			
		3. Internal Structures
		
		
			
	
	
	//	1. IS-A relationship (repreneted by a close-head arrow)
	//	
	//	2. Referencial Has-A relationship (repreneted by a open-head arrow with a quantifier "1" or "*")
	//	
	//	3. Mutual Has-A relationship (represented by a line with quantifiers like "1..*" in both ends)
	
	Abstract classes and methods are indicated by italicized text.
		
3. sequence

	A sequence diagram shows the work flow in two dimensions:
	
		the vertical dimension shows time, start from top
		
		the horizontal dimension shows classes, start from the left
		
		The work flow is now a sequence of calls from class to class. Each call is represented by an concrete arrow from the 
		sender to the receiver, and the return value is optionally represented by a dotted arrow of backward direction.

4. statechart

	Statechart is to represent the states of some classes of more than three states. 

5. activity

	An activity diagram is like a sequence diagram, but mostly used in high level modeling. 

6. component

	A component diagram represents a system with all the components.

7. deployment

	A deployment diagram involed both components and their deployment environments

