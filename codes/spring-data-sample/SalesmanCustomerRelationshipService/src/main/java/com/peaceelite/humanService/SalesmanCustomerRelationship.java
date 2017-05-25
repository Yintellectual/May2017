package com.peaceelite.humanService;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;
import java.util.*;
import javax.persistence.NamedQuery;



@Entity
@NamedQuery(name="SalesmanCustomerRelationship.findByNickName", 
	query="select scr from SalesmanCustomerRelationship scr where scr.nickName = ?1")
public class SalesmanCustomerRelationship{
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String firstName;
	private String lastName;
	private String nickName;
	
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getNickName(){
		return nickName;
	}
	
	public void setNickName(String nickName){
		this.nickName=nickName;
	}
	
	
	@DomainEvents
	Collection<Object> domainEvents() {
        List<Object> result = new ArrayList<Object>();
		result.add("Here should be an Event not a String, but, anyway");
		return result;
	}
	
	@AfterDomainEventPublication 
    void callbackMethod() {
		System.out.println("1111111111111111111111111111111111"+"WELL DONE");
	}

}