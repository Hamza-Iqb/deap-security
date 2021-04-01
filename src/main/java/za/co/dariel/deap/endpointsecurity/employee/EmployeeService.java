package za.co.dariel.deap.endpointsecurity.employee;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EmployeeService {

	private EmployeeRepository employeeRepo;
	
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	public List<Employee> getEmployees() {

		return (List<Employee>) employeeRepo.findAll();
	}

	public Employee getSingleEmployee(String username) {
		Optional<Employee> email = employeeRepo.findById(username);

		if (!email.isPresent()) {
			throw new IllegalStateException("Username/Account does not exist     ");
		}

		return employeeRepo.findById(username).orElse(null);
	}

	public void addEmployee(Employee employee) {
		Boolean userExists = employeeRepo.findById(employee.getEmail()).isPresent();
		
		if(userExists) {
			throw new IllegalStateException("Email is already taken");
	
		}
		
		String encodedPassword = bCryptPasswordEncoder.encode(employee.getPassword());
		
		employee.setPassword(encodedPassword);
		
		employeeRepo.save(employee);

	}

	@Transactional
	//annotation allows me to just use the setters to adjust values in database instead of having to ask repo to save again
	public void updateStudent(String username, String firstName, String lastName) {

		Employee employee = employeeRepo.findById(username)
				.orElseThrow(() -> new IllegalStateException("Employee with username: " + username + " does not exist"));

		if (firstName != null && firstName.length() > 0 && !Objects.equals(employee.getFirstName(), firstName)) {
			employee.setFirstName(firstName);
		}

		if (lastName != null && lastName.length() > 0 && !Objects.equals(employee.getLastName(), lastName)) {
			employee.setLastName(lastName);
		}
	}

}
