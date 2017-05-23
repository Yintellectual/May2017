1. Custom repository with custom methods

	Example:
	
		package com.peaceelite.humanService;

		import java.util.List;

		import org.springframework.data.repository.PagingAndSortingRepository;
		import org.springframework.data.repository.query.Param;
		import org.springframework.data.rest.core.annotation.RepositoryRestResource;
		import org.springframework.data.jpa.repository.Modifying;
		import org.springframework.data.jpa.repository.Query;
		import org.springframework.transaction.annotation.Transactional;

		@RepositoryRestResource(collectionResourceRel = "people", path = "people")
		public interface PersonRepository extends PagingAndSortingRepository<Person, Long> {

		List<Person> findByLastName(@Param("name") String name);

			@Modifying
			@Transactional
			@Query("update Person person set person.firstName = :firstName where person.lastName = :lastName")
			int setFirstNameForPerson(@Param("firstName") String firstName,@Param("lastName") String lastName);
		}