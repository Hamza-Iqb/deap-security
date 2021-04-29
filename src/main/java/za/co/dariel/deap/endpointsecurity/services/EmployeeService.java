package za.co.dariel.deap.endpointsecurity.services;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import za.co.dariel.deap.endpointsecurity.entities.EmployeeEntity;

@Service
@AllArgsConstructor
public class EmployeeService{

	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	private final String USER_NOT_FOUND_MSG = "EmployeeEntity with username: %s not found";

	private KeycloakService keyClockService;
	
	private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);


	public EmployeeEntity addEmployee(EmployeeEntity employee) {

		String userId = keyClockService.createUserInKeyCloak(employee);
		employee.setKeycloakId(userId);

		String encodedPassword = bCryptPasswordEncoder.encode(employee.getPassword());

		employee.setPassword(encodedPassword);

		return employee;

	}

//	@Transactional
//	// annotation allows me to just use the setters to adjust values in database
//	// instead of having to ask repo to save again
//	public EmployeeEntity updateStudent(String username, EmployeeEntity employeeRequest) {
//
//		EmployeeEntity employee = employeeRepo.findById(username).orElseThrow(
//				() -> new IllegalStateException("EmployeeEntity with username: " + username + " does not exist"));
//
//		if (employeeRequest.getFirstName() != null && employeeRequest.getFirstName().length() > 0
//				&& !Objects.equals(employee.getFirstName(), employeeRequest.getFirstName())) {
//			employee.setFirstName(employeeRequest.getFirstName());
//		}
//
//		if (employeeRequest.getLastName() != null && employeeRequest.getLastName().length() > 0
//				&& !Objects.equals(employee.getLastName(), employeeRequest.getLastName())) {
//			employee.setLastName(employeeRequest.getLastName());
//		}
//		return employeeRequest;
//	}


}
