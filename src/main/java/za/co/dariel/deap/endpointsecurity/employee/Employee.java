package za.co.dariel.deap.endpointsecurity.employee;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Employee {
	
	@Id
	private String email;
	private String firstName;
	private String lastName;
	private String password;
	
	
	public Employee(String email, String firstName, String lastName, String password) {
		super();
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
	}
	
	

}
