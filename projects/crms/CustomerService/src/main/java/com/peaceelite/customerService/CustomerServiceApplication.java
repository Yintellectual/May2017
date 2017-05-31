package com.peaceelite.customerService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import java.util.*;

//import org.springframework.context.annotation.ComponentScan;

//import com.peaceelite.pojoProfile.RequirementProfile;


@SpringBootApplication(	scanBasePackages={"com.peaceelite.customerService","com.peaceelite.pojoProfile"})
//@EnableJpaRepositories()
//@ComponentScan()
public class CustomerServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(CustomerServiceApplication.class, args);
	}
	
	
	@Bean
	public CommandLineRunner commandLineRunner(CustomerRepository customerRepository){
		return (args->{
				Customer customer = new Customer("Taylor");
				customerRepository.save(customer);
				
				Customer c = customerRepository.findOne(1L);
				c.addRequirementProfile(new RequirementProfile(2L,"test"));
				customerRepository.save(c);
		});
	}
}
