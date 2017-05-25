
Reference: http://docs.spring.io/spring-data/data-jpa/docs/current/reference/html/#jpa.modifying-queries

To be find out: 

	1. RevisionRepository

	2. RepositoryDefinition

	3. "4.3.2. Using Repositories with multiple Spring Data modules"

	4. "5. JPA Repositories"
	
			"5.3.5. Using Sort"
			
			"5.3.7. Using SpEL expressions"
			
			"5.3.9. Applying query hints"
			
			"5.3.10. Configuring Fetch- and LoadGraphs"

			"5.3.11. Projections"
			
			"5.4. Stored procedures"
			
			"5.5. Specifications"
			
			"5.6. Query by Example"
			
			"5.7. Transactionality"
			
			"5.8. Locking"
			
			"5.9. Auditing"
			
			"5.10. JPA Auditing"
	5. "6. Miscellaneous"
	
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
			
			A full table is "Table 4. Supported keywords inside method names" in:
			http://docs.spring.io/spring-data/data-jpa/docs/current/reference/html/#jpa.query-methods
			
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
	
			1. Query lookup strategies
			
			2. Query creation
			
			3. Using JPA NamedQueries
	
				e.g.:
				
				@Entity
				@javax.persistence.NamedQuery(name = "User.findByEmailAddress",
					query = "select u from User u where u.emailAddress = ?1")
				public class User {

				}
			
			
				public interface UserRepository extends JpaRepository<User, Long> {

					List<User> findByLastname(String lastname);

					User findByEmailAddress(String emailAddress);
				}
				
				
			
			4. Using @Query
			
				4.1 The difference between @NamedQuery and @Query is that @NamedQuery applies on an Entity while @Query applies
				on a method in some repository interface. 
			
				4.2 Advanced LIKE expression can be used in @Query. SQL LIKE is described here: 
				
					https://www.w3schools.com/sql/sql_like.asp
					
				4.3 Native Queries:
				
					public interface UserRepository extends JpaRepository<User, Long> {

						@Query(value = "SELECT * FROM USERS WHERE EMAIL_ADDRESS = ?1", nativeQuery = true)
						User findByEmailAddress(String emailAddress);
					}
				
				4.4 Sort`
			
			5. Using Sort
			
			6. Using named parameters
			
			org.springframework.data.repository.query.Param is required for customer method parameter names.
			
			Once a parameter is assigned with a name, the parameter name can be used in Query. E.g.:
			
				@Query("select u from User u where u.firstname = :firstname or u.lastname = :lastname")
				User findByLastnameOrFirstname(@Param("lastname") String lastname,
					@Param("firstname") String firstname);
			 
			
			7. Using SpEL expressions
			
			8. Modifying queries
	
				@Modifying 

					means we are modifying an existing row, and only part of the information is necessary
			
			9. Applying query hints
			
			10. Configuring Fetch and loadGraphs
			
				configures if eager or lazy fetch
			
			11. Projections
			
				Projections are interfaces that contains only get methods to some of the properties of an Entity.
				
				The purpose is to fetch only part of the Entity information.
				
				To use a projection, set it as return type of a method in a repository interface.
				
				The property names used by a projection can be reassigned by  @Value("#{target.lastName}")
			
			
	
Part 3. Public Events from Entity Class (Aggregate Roots)

		https://stackoverflow.com/questions/42733163/example-for-domainevents-and-afterdomaineventspublication/44176258#44176258
		
Part 4. Extensions:

	1. Querydsl: fluent API
	
	2. Web Support 
	
	3. Repository populators
	
	4. Legacy Web Support
	


	
	