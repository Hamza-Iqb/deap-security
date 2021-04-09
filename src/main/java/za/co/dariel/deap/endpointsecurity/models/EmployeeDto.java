package za.co.dariel.deap.endpointsecurity.models;

import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EmployeeDto {

	@Id
	private String email;
	private String firstName;
	private String lastName;
	private String password;

	public EmployeeDto(String email, String firstName, String lastName, String password) {
			super();
			this.email = email;
			this.firstName = firstName;
			this.lastName = lastName;
			this.password = password;
		}

}
