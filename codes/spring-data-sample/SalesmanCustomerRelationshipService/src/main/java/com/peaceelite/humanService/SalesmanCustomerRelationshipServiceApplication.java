package com.peaceelite.humanService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import java.util.*;
@SpringBootApplication
//@EnableJpaRepositories
public class SalesmanCustomerRelationshipServiceApplication {

	@Autowired
	CrudRepository crudRepository;
	
	
	public static void main(String[] args) {
		SpringApplication.run(SalesmanCustomerRelationshipServiceApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner commandLineRunner(SalesmanCustomerRelationshipRepository salesmanCustomerRelationshipRepository){
		return (args->{
				SalesmanCustomerRelationship scr = new SalesmanCustomerRelationship();
				scr.setFirstName("Paul");
				scr.setLastName("Beauty");
				scr.setNickName("The Intellectual");
				crudRepository.save(scr);
				
				List<SalesmanCustomerRelationship> srcs= salesmanCustomerRelationshipRepository.findByNickName("The Intellectual");
				System.out.println("0000000000000000000000000 There are "+ srcs.size()+" intellectuals");
			
		});
	}
	
}
