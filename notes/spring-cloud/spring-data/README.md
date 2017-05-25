
Reference: http://docs.spring.io/spring-data/data-jpa/docs/current/reference/html/#jpa.modifying-queries

To be find out: 

	1. RevisionRepository

	2. RepositoryDefinition

	3. "4.3.2. Using Repositories with multiple Spring Data modules"
	
The most amazing thing of Java Data is providing a "repository" layer. The repository layer consists of some interfaces.
There interfaces extends from some Repository interfaces provided by Java Data. Java Data Repository interfaces contains 
few core methods. However, we can add or modify the methods in our custom interfaces. 

Part 1. Java Data Repositorys:

	org.springframework.data.repository.Repository<T,ID extends Serializable>,
	org.springframework.data.repository.CrudRepository<T,ID extends Serializable> extends Repository<T,ID extends Serializable>, 
	org.springframework.data.repository.PagingAndSortingRepository<T,ID extends Serializable> extends CrudRepository<T,ID extends Serializable>, 
	org.springframework.data.repository.RevisionRepository<T,ID extends Serializable,N extends Number & Comparable<N>> 
			extends Repository<T,ID extends Serializable>
	
	JpaRepository extends CrudRepository
	MongoRepository extends CrudRepository
	
	1.1 Repository<T, ID>: 
	
		Merely a marker interfaces
		
	1.2 CrudRepository<T, ID>(6 important methods and more)
	
		<S extends T> S save(S entity);
		
		T findOne(ID primaryKey);
		
		Iterable<T> findAll();
		
		Long count();
		
		void delete(T entity);
		
		boolean exists(ID primaryKey);
		
	1.3 PagingAndSortingRepository<T, ID>(2 important methods and no more)
		
		Iterable<T> findAll(Sort sort);
		
		Page<T> findAll(Pageable pageable);

		
	1.4 RevisionRepository?? remains mistery to me.
	
Part 2. Custom Repositories with Methods 

	2.1 Hiding default methods:
	
		Use @NoRepositoryBean, Example 7 in http://docs.spring.io/spring-data/data-jpa/docs/current/reference/html/

	
	2.2.0 Configure Query Lookup Strategies

		Since there are two strategies for Java Data infrastructure to construct queries for methods declaration, a 
		configuration mechanism is provided.
		
		There are three strategies to choose from:
		
			1. CREATE : use name convention
			2. USE_DECLARED_QUERY : use @Query and throw Exception if not found
			3. CREATE_IF_NOT_FOUND : use name convention if @Query is not found
		
		To configure it, @org.springframework.data.jpa.repository.config.EnableJpaRepositories(queryLookupStrategy = 
				org.springframework.data.repository.query.QueryLookupStrategy.Key(.CREATE/USE_DECLARED_QUERY/CREATE_IF_NOT_FOUND))
		
	2.2 Adding Custom Methods Using Java Data Method Naming Convention
	
	In addtion to the methods in Java Data Repository Interfaces, custom methods can be declared in custom repository 
	that extends org.springframework.data.repository.Repository<T,ID extends Serializable> and implemented by Java Data
	if the naming convention is followed.

	Java Data Method Naming Convention:
	
		a. The naming convention is of one of the structures below:
		
			find{introducing clause*}By{criteria clause**}
			read...By...
			query...By...
			count...By...
			get...By...
			delete...By...
			*An introducing clause can be "DistinctPeople" or "PeopleDistinct"
			**criteria clause contains properties and the logical relationship among them.
			
			The criteria consists of property names and logic expressions.
		
		b. Introducing clause:
		
			"DistinctPeople" or "PeopleDistinct"
			"findFirstByOrderByLastnameAsc"
			"findTopByOrderByAgeDesc"
			"First10"
			"Top3"
			
			
		
		c. The properties can be entity property or the property of an entity property(nested properties).
		
			e.g. 
			
				List<Person> findByAddressZipCode(ZipCode zipCode);
			
			or 
			
				List<Person> findByAddress_ZipCode(ZipCode zipCode);
			
		d. The logic expression can be "And", "Or", "IgnoreCase", "AllIgnoreCase", "OrderBy...Asc", "OrderBy...Desc", "Between", 
		"LessThan", "GreaterThan", "Like".
			
			
		e. Streaming:
		
			@Query("select u from User u")
			Stream<User> findAllByCustomQueryAndStream();
 
			Stream<User> readAllByFirstnameNotNull();

			@Query("select u from User u")
			Stream<User> streamAllPaged(Pageable pageable);
	
			try (Stream<User> stream = repository.findAllByCustomQueryAndStream()) {
				stream.forEach(…);
			}
	
		f. Async query
		
			@Async
			Future<User> findByFirstname(String firstname);               

			@Async
			CompletableFuture<User> findOneByFirstname(String firstname); 

			@Async
			ListenableFuture<User> findOneByLastname(String lastname);    
	
	2.3 Adding Custom Methods Using @Query
	
	
Part 3. Public Events from Entity Class (Aggregate Roots)

		https://stackoverflow.com/questions/42733163/example-for-domainevents-and-afterdomaineventspublication/44176258#44176258
		
		